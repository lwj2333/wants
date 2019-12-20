package com.lwj.wants.recyclerview

import android.app.Activity
import android.content.Context
import android.support.v4.widget.ViewDragHelper.INVALID_POINTER
import android.util.AttributeSet

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.lwj.wants.R
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_DISTANCE_UNDEFINED
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_MODE_BOTH
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_MODE_BOTTOM
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_MODE_TOP
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.STATE_FINISH_DOWN
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.STATE_FINISH_TOP
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.STATE_MOVE
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.STATE_UP
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.VIEW_BOTTOM
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.VIEW_TOP

import com.lwj.wants.recyclerview.util.Instrument
import java.util.*


/**
 * @author by  LWJ
 * @date on 2018/10/24
 * @describe 添加描述
 * @org  http://www.gdjiuji.com(广东九极生物科技有限公司)
 */
open class SlidingLayout : ViewGroup {

    object SlidingLayout {
        val SLIDING_MODE_BOTH = 0//下拉和上滑
        /**
         * 下拉
         */
        val SLIDING_MODE_TOP = 1
        /**
         * 上滑
         */
        val SLIDING_MODE_BOTTOM = 2

        val STATE_MOVE = 2//滑动
        val STATE_UP = 1//抬起
        val STATE_FINISH_TOP = 3//刷新完成
        val STATE_FINISH_DOWN = 4//加载完成


        val VIEW_TOP = 0
        val VIEW_BOTTOM = 1
        val SLIDING_DISTANCE_UNDEFINED = -1
    }

    private var refreshStats: Stats = Stats.DEFAULT
    private var loadStats: Stats = Stats.DEFAULT


    private var resetDuration = 200//自动回弹持续时间
    private var smoothDuration = 200//移动持续时间
    private var delayDuration = 1000//延迟持续时间

    private var mTouchSlop: Int = 0//系统允许最小的滑动判断值
    private var mBackgroundViewLayoutId = 0
    private var mTopViewId = 0
    private var mBottomViewId = 0

    private var mBackgroundView: View? = null//背景View
    private var mTargetView: View? = null//前景View
    private var topView: View? = null//顶部View
    private var bottomView: View? = null//底部View

    private var mIsBeingDragged: Boolean = false//是否拦截事件
    private var mInitialDownY: Float = 0f //初始 按下的 Y位置
    private var mInitialMotionY: Float = 0f// 启动滑动时的Y位置
    private var mLastMotionY: Float = 0f//最后 按下的 Y位置
    var refreshStateY = 0f//刷新状态值
    var loadStateY = 0f//加载状态值
    private var defaultTouchDistance: Float = 0f//默认刷新值 当没有顶部View 或者 底部View

    private var mActivePointerId = INVALID_POINTER

    private var mSlidingOffset = 0.5f//滑动阻力系数

    private var mSlidingMode = SLIDING_MODE_BOTH


    private var mSlidingListener: SlidingListener? = null


    private var mSlidingTopMaxDistance = SLIDING_DISTANCE_UNDEFINED
    private var mSlidingBottomMaxDistance = SLIDING_DISTANCE_UNDEFINED

    private var mDelegateTouchListener: OnTouchListener? = null


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs)
    }

    open fun initView() {}
    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingLayout)
        mBackgroundViewLayoutId = typedArray.getResourceId(
            R.styleable.SlidingLayout_sl_background_view,
            mBackgroundViewLayoutId
        )
        mTopViewId = typedArray.getResourceId(R.styleable.SlidingLayout_sl_top_view, mTopViewId)
        mBottomViewId =
            typedArray.getResourceId(R.styleable.SlidingLayout_sl_bottom_view, mBottomViewId)
        mSlidingMode = typedArray.getInteger(R.styleable.SlidingLayout_sl_mode, SLIDING_MODE_BOTH)

        mSlidingTopMaxDistance = typedArray.getDimensionPixelSize(
            R.styleable.SlidingLayout_sl_top_max,
            SLIDING_DISTANCE_UNDEFINED
        )
        mSlidingBottomMaxDistance = typedArray.getDimensionPixelSize(
            R.styleable.SlidingLayout_sl_bottom_max,
            SLIDING_DISTANCE_UNDEFINED
        )
        defaultTouchDistance =
            typedArray.getDimensionPixelSize(R.styleable.SlidingLayout_sl_defaultTouchDistance, 400)
                .toFloat()
        typedArray.recycle()
        if (mTopViewId != 0) {
            val v = View.inflate(context, mTopViewId, null)
            setTopView(v)
        }
        if (mBottomViewId != 0) {
            val v = View.inflate(context, mBottomViewId, null)
            setBottomView(v)
        }
        if (mBackgroundViewLayoutId != 0) {
            val v = View.inflate(context, mBackgroundViewLayoutId, null)
            setBackgroundView(v)
        }

        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        initView()
    }

    fun setBackgroundView(v: View) {
        if (mBackgroundView != null) {
            this.removeView(mBackgroundView)
        }
        mBackgroundView = v
        this.addView(v, 0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

    }

    fun setTopView(v: View) {
        if (topView != null) {
            this.removeView(topView)
        }
        topView = v
        this.addView(v, 0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    fun setBottomView(v: View) {
        if (bottomView != null) {
            this.removeView(bottomView)
        }
        bottomView = v
        this.addView(v, 0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

    }

    protected fun setRefreshStats(state: Stats) {
        this.refreshStats = state
    }

    fun getRefreshStats(): Stats {
        return refreshStats
    }

    protected fun setLoadStats(state: Stats) {
        this.loadStats = state
    }

    fun getLoadStats(): Stats {
        return loadStats
    }

    fun getBackgroundView(): View? {
        return mBackgroundView
    }

    fun getTopView(): View? {
        return topView
    }

    fun getBottomView(): View? {
        return bottomView
    }

    /**
     * @param distance 设置顶部的最大滑动距离
     */
    fun setSlidingTopMaxDistance(distance: Int) {
        this.mSlidingTopMaxDistance = distance
    }

    fun getSlidingTopMaxDistance(): Int {
        return mSlidingTopMaxDistance
    }

    fun getSlidingBottomMaxDistance(): Int {
        return mSlidingBottomMaxDistance
    }

    fun setSlidingBottomMaxDistance(mSlidingBottomMaxDistance: Int) {
        this.mSlidingBottomMaxDistance = mSlidingBottomMaxDistance
    }

    fun getSlidingOffset(): Float {
        return mSlidingOffset
    }

    /**
     * @param offset 设置滑动幅度
     */
    fun setSlidingOffset(offset: Float) {
        this.mSlidingOffset = offset
    }

    /**
     * @param listener 设置滑动监听
     */
    fun setSlidingListener(listener: SlidingListener) {
        this.mSlidingListener = listener
    }

    /**
     * @param listener 获取滑动监听
     */
    fun getSlidingListener(): SlidingListener? {
        return mSlidingListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        if (childCount == 0) {
            return
        }
        if (mTargetView == null) {
            ensureTarget()
        }
        if (mTargetView == null) {
            return
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onLayout(b: Boolean, left: Int, top: Int, right: Int, bottom: Int) {


        if (mBackgroundView != null) {
            val childWidth = mBackgroundView!!.measuredWidth
            val childHeight = mBackgroundView!!.measuredHeight
            mBackgroundView!!.layout(0, 0, childWidth, childHeight)
        }
        if (topView != null) {
            val childTVWidth = topView!!.measuredWidth
            val childTVHeight = topView!!.measuredHeight
            if (refreshStateY == 0f) {
                refreshStateY = childTVHeight.toFloat()
            }
            topView!!.layout(0, -childTVHeight, childTVWidth, 0)
        }
        if (bottomView != null) {
            val childBVWidth = bottomView!!.measuredWidth
            val childBVHeight = bottomView!!.measuredHeight
            if (loadStateY == 0f) {
                loadStateY = childBVHeight.toFloat()
            }
            bottomView!!.layout(
                0,
                mTargetView!!.measuredHeight,
                childBVWidth,
                childBVHeight + mTargetView!!.measuredHeight
            )
        }
        if (mTargetView != null) {
            val childTVWidth = mTargetView!!.measuredWidth
            val childTVHeight = mTargetView!!.measuredHeight
            mTargetView!!.layout(0, 0, childTVWidth, childTVHeight)
        }
    }

    private fun ensureTarget() {
        if (mTargetView == null) {
            mTargetView = this.getChildAt(childCount - 1)
        }

    }

    fun setResetDuration(time: Int) {
        resetDuration = time
    }

    fun setSmoothDuration(time: Int) {
        smoothDuration = time
    }

    fun setDelayDuration(time: Int) {
        delayDuration = time
    }

    fun setTargetView(v: View) {
        if (mTargetView != null) {
            this.removeView(mTargetView)
        }
        mTargetView = v
        this.addView(v)
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        // super.setOnTouchListener(l);
        mDelegateTouchListener = l
    }

    fun getTargetView(): View? {
        return mTargetView
    }

    fun getSlidingDistance(): Float {
        return getInstrument().getTransLationY(getTargetView())
    }

    fun getInstrument(): Instrument {
        return Instrument.getInstance()
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0)
                mIsBeingDragged = false
                val initialDownY = getMotionEventY(ev, mActivePointerId)

                if (initialDownY == -1f) {
                    return false
                }
                mInitialDownY = initialDownY
            }
            MotionEvent.ACTION_MOVE -> {
                if (mActivePointerId == INVALID_POINTER) {
                    return false
                }
                val y = getMotionEventY(ev, mActivePointerId)

                if (y == -1f) {
                    return false
                }

                if (y > mInitialDownY) {
                    //判断是否是下滑刷新
                    val yDiff = y - mInitialDownY
                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollUp()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop
                        mLastMotionY = mInitialMotionY
                        mIsBeingDragged = true
                    }
                } else if (y < mInitialDownY) {
                    //判断是否是上拉加载
                    val yDiff = mInitialDownY - y

                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollDown()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop
                        mLastMotionY = mInitialMotionY
                        mIsBeingDragged = true
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
            }
        }
        return mIsBeingDragged
    }

    private fun getMotionEventY(ev: MotionEvent, activePointerId: Int): Float {
        val index = ev.findPointerIndex(activePointerId)
        return if (index < 0) {
            -1f
        } else ev.getY(index)
    }

    /**
     * @return 判断子控件是否可以向下滑动   -1 下滑
     */
    private fun canChildScrollUp(): Boolean {

        return mTargetView!!.canScrollVertically(-1)
    }

    /**
     * @return 判断子控件是否可以向上滑动   1 上拉
     */
    private fun canChildScrollDown(): Boolean {
        return mTargetView!!.canScrollVertically(1)
    }

    private fun isCanSliding(locationY: Float): Boolean {

        if (locationY > mInitialDownY) {
            //判断是否是下滑刷新
            val yDiff = locationY - mInitialDownY
            if (yDiff > mTouchSlop) {
                mInitialMotionY = mInitialDownY + mTouchSlop
                mLastMotionY = mInitialMotionY
                return true
            }
        } else if (locationY < mInitialDownY) {

            //判断是否是上拉加载
            val yDiff = mInitialDownY - locationY
            if (yDiff > mTouchSlop) {
                mInitialMotionY = mInitialDownY - mTouchSlop
                mLastMotionY = mInitialMotionY
                return true
            }
        }
        return false
    }

    private var oneselfDown = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mDelegateTouchListener != null && mDelegateTouchListener!!.onTouch(this, event)) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                oneselfDown = true
            }
            MotionEvent.ACTION_MOVE -> {
                //   Log.i(TAG, "SlidingLayout_onTouchEvent: ${event.y} ACTION_MOVE ")
                val activePointerId = event.getPointerId(0)
                if (oneselfDown) {
                    if (!isCanSliding(event.y)) {
                        return true
                    } else {
                        oneselfDown = false
                    }
                }
                var delta = 0f
                //双手指 矫正偏差
                if (mActivePointerId != activePointerId) {
                    mActivePointerId = activePointerId
                    mInitialDownY = getMotionEventY(event, mActivePointerId)
                    mInitialMotionY = mInitialDownY
                    mLastMotionY = mInitialMotionY

                    if (mSlidingListener != null) {
                        mSlidingListener!!.onSlidingChangePointer(mTargetView!!, activePointerId)
                    }
                    // Log.e(TAG, "SlidingLayout_onTouchEvent: $ 多   $mInitialDownY $mInitialMotionY ")
                    return true
                } else {
                    //计算速度
                    delta = getMotionEventY(event, mActivePointerId) - mLastMotionY
                }
                // Log.e(TAG, "SlidingLayout_onTouchEvent:偏差 $delta    ")
                val tempOffset =
                    1 - (Math.abs(getInstrument().getTransLationY(mTargetView) + delta) / mTargetView!!.measuredHeight)
                delta =
                    getInstrument().getTransLationY(mTargetView) + delta * mSlidingOffset * tempOffset

                mLastMotionY = getMotionEventY(event, mActivePointerId)
                val move: Float = getMotionEventY(event, mActivePointerId) - mInitialMotionY
                val distance = getSlidingDistance()
                when (mSlidingMode) {
                    SLIDING_MODE_BOTH -> if (move >= 0) {
                        slidingToBottom(move, distance, delta)
                    } else {
                        slidingToTop(move, distance, delta)
                    }
                    SLIDING_MODE_TOP -> slidingToBottom(move, distance, delta)
                    SLIDING_MODE_BOTTOM -> slidingToTop(move, distance, delta)
                }
                if (mSlidingListener != null) {
                    mSlidingListener!!.onSlidingStateChange(this, STATE_MOVE)
                    mSlidingListener!!.onSlidingOffset(this, delta)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mSlidingListener != null) {
                    mSlidingListener!!.onSlidingStateChange(this, STATE_UP)
                }
                slidingToReset()
            }
        }
        return true
    }

    fun slidingToReset() {
        when {
            getSlidingDistance() > 0 -> {
                getInstrument().reset(mTargetView, resetDuration.toLong())
                getInstrument().reset(topView, resetDuration.toLong())
            }
            getSlidingDistance() < 0 -> {
                getInstrument().reset(mTargetView, resetDuration.toLong())
                getInstrument().reset(bottomView, resetDuration.toLong())
            }
            else -> {
            }
        }
    }

    fun slidingToRefresh() {
        when {
            getSlidingDistance() > 0 -> {
                if (getSlidingDistance() >= refreshStateY) {
                    if (topView != null) {
                        smoothScrollTo(VIEW_TOP, refreshStateY)
                    } else {
                        slidingToReset()
                    }
                } else {
                    slidingToReset()
                }
            }
            getSlidingDistance() < 0 -> {
                if (getSlidingDistance() <= -loadStateY) {
                    if (bottomView != null) {
                        smoothScrollTo(VIEW_BOTTOM, -loadStateY)
                    } else {
                        slidingToReset()
                    }
                } else {
                    slidingToReset()
                }
            }
            else -> {
            }
        }
    }

    private val TAG = "SlidingLayout"
    private fun slidingToBottom(move: Float, distance: Float, d: Float) {

        var delta = d
        when {
            move > 0 -> {
                //向下滑动
                if (mSlidingTopMaxDistance == SLIDING_DISTANCE_UNDEFINED || delta < mSlidingTopMaxDistance) {
                    //滑动范围内 没有设置滑动距离
                } else {
                    //超过滑动范围
                    delta = mSlidingTopMaxDistance.toFloat()
                }
                if (distance < 0) {

                    getInstrument().slidingByDeltaToY(bottomView, delta)
                } else {

                    getInstrument().slidingByDeltaToY(topView, delta)
                }
                getInstrument().slidingByDeltaToY(mTargetView, delta)

            }
            mSlidingMode==SLIDING_MODE_TOP && move < 0 && distance > 0 -> {
                if (delta < 0) {
                    delta = 0f
                }

                getInstrument().slidingByDeltaToY(topView, delta)
                getInstrument().slidingByDeltaToY(mTargetView, delta)
            }
        }
    }

    /**
     *   向上滑动
     */
    private fun slidingToTop(move: Float, distance: Float, d: Float) {

        var delta = d
        if (move < 0) {
            //向上滑动
            if (mSlidingBottomMaxDistance == SLIDING_DISTANCE_UNDEFINED || delta > -mSlidingBottomMaxDistance) {
                //滑动范围内或 没有设置最大滑动距离
            } else {
                //超过滑动范围
                delta = (-mSlidingBottomMaxDistance).toFloat()
            }

            if (getSlidingDistance() > 0) {
                getInstrument().slidingByDeltaToY(topView, delta)
            } else {
                getInstrument().slidingByDeltaToY(bottomView, delta)
            }
            getInstrument().slidingByDeltaToY(mTargetView, delta)

        }
    }

    fun getmSlidingMode(): Int {
        return mSlidingMode
    }

    fun setmSlidingMode(mode: Int) {
        this.mSlidingMode = mode
    }

    fun smoothScrollTo(id: Int, y: Float) {
        when (id) {
            VIEW_TOP -> getInstrument().smoothToY(topView, y, smoothDuration.toLong())
            VIEW_BOTTOM -> getInstrument().smoothToY(bottomView, y, smoothDuration.toLong())
        }
        getInstrument().smoothToY(mTargetView, y, smoothDuration.toLong())

    }

    /**
     * 停止
     */
    fun stop(id: Int) {

        when (id) {
            VIEW_TOP -> if (topView != null) {
                mSlidingListener?.onSlidingStateChange(this@SlidingLayout, STATE_FINISH_TOP)
                delayTack(Runnable {
                    Instrument.getInstance()
                        .reset(mTargetView, resetDuration.toLong())
                    Instrument.getInstance().reset(topView, resetDuration.toLong())
                })
            }
            VIEW_BOTTOM -> if (bottomView != null) {
                mSlidingListener?.onSlidingStateChange(this@SlidingLayout, STATE_FINISH_DOWN)
                delayTack(Runnable {
                    Instrument.getInstance()
                        .reset(mTargetView, resetDuration.toLong())
                    Instrument.getInstance()
                        .reset(bottomView, resetDuration.toLong())
                })
            }
        }
    }

    private var timer: Timer? = Timer()

    private fun delayTack(r: Runnable) {
        val task = object : TimerTask() {
            override fun run() {
                (context as Activity).runOnUiThread(r)
            }
        }
        if (timer != null) {
            timer!!.schedule(task, delayDuration.toLong())
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnima(mTargetView)
        clearAnima(topView)
        clearAnima(bottomView)
        mSlidingMode = 0
        mTargetView = null
        mBackgroundView = null
        topView = null
        mSlidingListener = null
        timer = null
    }

    private fun clearAnima(v: View?) {
        v?.clearAnimation()
    }


    interface SlidingListener {
        /**
         * @param delta 滑动距离
         */
        fun onSlidingOffset(v: View, delta: Float)

        /**
         * @param state 滑动状态
         */
        fun onSlidingStateChange(v: View, state: Int)

        fun onSlidingChangePointer(v: View, pointerId: Int)
    }

    enum class Stats {
        RUN, COMPLETE, DEFAULT, READY,
    }
}