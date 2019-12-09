package com.lwj.wants.recyclerview

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.support.v4.app.FrameMetricsAggregator.DELAY_DURATION
import android.support.v4.widget.ViewDragHelper.INVALID_POINTER
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.lwj.wants.R
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.RESET_DURATION
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_DISTANCE_UNDEFINED
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_MODE_BOTH
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_MODE_BOTTOM
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SLIDING_MODE_TOP
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.SMOOTH_DURATION
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.STATE_IDLE
import com.lwj.wants.recyclerview.SlidingLayout.SlidingLayout.STATE_SLIDING
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
class SlidingLayout : ViewGroup {

    object SlidingLayout {
        val RESET_DURATION = 200//自动回弹持续时间
        val SMOOTH_DURATION = 200//移动持续时间
        val DELAY_DURATION = 200//延迟持续时间
        val SLIDING_MODE_BOTH = 0//下拉和上滑

        /**
         * 下拉
         */
        val SLIDING_MODE_TOP = 1
        /**
         * 上滑
         */
        val SLIDING_MODE_BOTTOM = 2


        private val INVALID_POINTER = -1

        val STATE_SLIDING = 2//滑动
        val STATE_IDLE = 1//停止

        val VIEW_TOP = 0
        val VIEW_BOTTOM = 1

        val SLIDING_DISTANCE_UNDEFINED = -1
    }

    private var mTouchSlop: Int = 0//系统允许最小的滑动判断值
    private var mBackgroundViewLayoutId = 0
    private var mTopViewId = 0
    private var mBottomViewId = 0

    private var mBackgroundView: View? = null//背景View
    private var mTargetView: View? = null//前景View
    private var topView: OutsideView? = null//顶部View
    private var bottomView: OutsideView? = null//底部View

    private var mIsBeingDragged: Boolean = false//是否拦截事件
    private var mInitialDownY: Float = 0f //初始 按下的 Y位置
    private var mInitialMotionY: Float = 0f// 启动滑动时的Y位置
    private var mLastMotionY: Float = 0f//最后 按下的 Y位置
    private var refreshStateY = 0f//刷新状态值
    private var loadStateY = 0f//加载状态值
    private var defaultTouchDistance: Float = 0f//默认刷新值 当没有顶部View 或者 底部View

    private var mActivePointerId = INVALID_POINTER

    private var mSlidingOffset = 0.5f//滑动阻力系数

    private var mSlidingMode = SLIDING_MODE_BOTH


    private var mSlidingListener: SlidingListener? = null
    private var animaListener: Animator.AnimatorListener? = null
    private var actionListener: ActionListener? = null

    private var mSlidingTopMaxDistance = SLIDING_DISTANCE_UNDEFINED
    private var mSlidingBottomMaxDistance = SLIDING_DISTANCE_UNDEFINED

    private var mDelegateTouchListener: OnTouchListener? = null


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs)
    }

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
        topView = v as OutsideView
        this.addView(v, 0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    fun setBottomView(v: View) {
        if (bottomView != null) {
            this.removeView(bottomView)
        }
        bottomView = v as OutsideView
        this.addView(v, 0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

    }

    fun getBackgroundView(): View? {
        return mBackgroundView
    }

    fun getTopView(): View? {
        return topView
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

    fun getmSlidingBottomMaxDistance(): Int {
        return mSlidingBottomMaxDistance
    }

    fun setmSlidingBottomMaxDistance(mSlidingBottomMaxDistance: Int) {
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
        animaListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                mSlidingListener!!.onSlidingStateChange(this@SlidingLayout, STATE_IDLE)
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        }
    }

    fun setActionListener(listener: ActionListener) {
        this.actionListener = listener
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
            refreshStateY = childTVHeight.toFloat()
            topView!!.layout(0, -childTVHeight, childTVWidth, 0)
        }
        if (bottomView != null) {
            val childBVWidth = bottomView!!.measuredWidth
            val childBVHeight = bottomView!!.measuredHeight
            loadStateY = childBVHeight.toFloat()
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

    fun setTargetView(v: View) {
        if (mTargetView != null) {
            this.removeView(mTargetView)
        }
        mTargetView = v
        this.addView(v)
    }

    override fun setOnTouchListener(l: View.OnTouchListener) {
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

    private val TAG = "SlidingLayout"

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "SlidingLayout_onInterceptTouchEvent:按下 ${ev.y}  ")
                mActivePointerId = ev.getPointerId(0)
                mIsBeingDragged = false
                val initialDownY = getMotionEventY(ev, mActivePointerId)

                if (initialDownY == -1f) {
                    return false
                }
                mInitialDownY = initialDownY
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, "SlidingLayout_onInterceptTouchEvent: 滑动 $${ev.y}  ")
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
                var delta: Float = 0f
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
                }else{
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

             //   Log.i(TAG, "SlidingLayout_onTouchEvent: $tempOffset $delta $move  ")
                val distance = getSlidingDistance()
            //    Log.e(TAG,"SlidingLayout_onTouchEvent: $distance $move $delta  ")
                when (mSlidingMode) {
                    SLIDING_MODE_BOTH -> if (move >= 0) {
                        slidingTOBottom(move, distance, delta)
                    } else {
                        slidingToTop(move, distance, delta)
                    }
                    SLIDING_MODE_TOP -> slidingTOBottom(move, distance, delta)
                    SLIDING_MODE_BOTTOM -> slidingToTop(move, distance, delta)
                }
                if (mSlidingListener != null) {
                    mSlidingListener!!.onSlidingStateChange(this, STATE_SLIDING)
                    mSlidingListener!!.onSlidingOffset(this, delta)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (getSlidingDistance() >= 0) {
                    if (getSlidingDistance() >= refreshStateY) {
                        if (topView != null) {
                            topView!!.run()
                            smoothScrollTo(VIEW_TOP, refreshStateY)
                            if (actionListener != null) {
                                actionListener!!.onRefresh()
                            }
                        } else {
                            if (getSlidingDistance() >= defaultTouchDistance) {
                                if (actionListener != null) {
                                    actionListener!!.onRefresh()
                                }
                            }
                            getInstrument().reset(
                                mTargetView,
                                RESET_DURATION.toLong(),
                                animaListener
                            )
                            getInstrument().reset(topView, RESET_DURATION.toLong(), animaListener)
                        }

                    } else {
                        getInstrument().reset(mTargetView, RESET_DURATION.toLong(), animaListener)
                        getInstrument().reset(topView, RESET_DURATION.toLong(), animaListener)
                    }
                } else {

                    if (getSlidingDistance() <= -loadStateY) {
                        if (bottomView != null) {
                            bottomView!!.run()
                            smoothScrollTo(VIEW_BOTTOM, -loadStateY)
                            if (actionListener != null) {
                                actionListener!!.onLoad()
                            }
                        } else {
                            if (getSlidingDistance() <= -defaultTouchDistance) {
                                if (actionListener != null) {
                                    actionListener!!.onLoad()
                                }
                            }
                            getInstrument().reset(
                                mTargetView,
                                RESET_DURATION.toLong(),
                                animaListener
                            )
                            getInstrument().reset(
                                bottomView,
                                RESET_DURATION.toLong(),
                                animaListener
                            )
                        }

                    } else {
                        getInstrument().reset(mTargetView, RESET_DURATION.toLong(), animaListener)
                        getInstrument().reset(bottomView, RESET_DURATION.toLong(), animaListener)
                    }

                }
            }

        }

        return true
    }

    private fun slidingTOBottom(move: Float, distance: Float, d: Float) {
        var delta = d
        if (move >= 0 || distance > 0) {
            //向下滑动
            if (delta < 0) {
                //如果还往上滑，就让它归零
                delta = 0f
            }
            if (topView != null) {
                if (distance >= refreshStateY) {
                    topView!!.action()
                } else {
                    topView!!.hint()
                }
            }


            if (mSlidingTopMaxDistance == SLIDING_DISTANCE_UNDEFINED || delta < mSlidingTopMaxDistance) {
                //滑动范围内 没有设置滑动距离
            } else {
                //超过滑动范围
                delta = mSlidingTopMaxDistance.toFloat()
            }
            getInstrument().slidingByDeltaToY(mTargetView, delta)
            getInstrument().slidingByDeltaToY(topView, delta)
        }
    }

    private fun slidingToTop(move: Float, distance: Float, d: Float) {
        var delta = d
        if (move <= 0 || distance < 0) {
            //向上滑动
            if (delta > 0) {
                //如果还往下滑,就让它归零
                delta = 0f
            }
            if (bottomView != null) {
                if (distance <= -loadStateY) {
                    bottomView!!.action()
                } else {
                    bottomView!!.hint()
                }
            }
            if (mSlidingBottomMaxDistance == SLIDING_DISTANCE_UNDEFINED || delta > -mSlidingBottomMaxDistance) {
                //滑动范围内 没有设置滑动距离
            } else {
                //超过滑动范围
                delta = (-mSlidingBottomMaxDistance).toFloat()
            }
            getInstrument().slidingByDeltaToY(mTargetView, delta)
            getInstrument().slidingByDeltaToY(bottomView, delta)
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
            VIEW_TOP -> getInstrument().smoothToY(topView, y, SMOOTH_DURATION.toLong())
            VIEW_BOTTOM -> getInstrument().smoothToY(bottomView, y, SMOOTH_DURATION.toLong())
        }
        getInstrument().smoothToY(mTargetView, y, SMOOTH_DURATION.toLong())

    }

    /**
     * 停止
     */
    fun stop(id: Int) {

        when (id) {
            VIEW_TOP -> if (topView != null) {
                topView!!.finish()
                delayTack(Runnable {
                    Instrument.getInstance()
                        .reset(mTargetView, RESET_DURATION.toLong(), animaListener)
                    Instrument.getInstance().reset(topView, RESET_DURATION.toLong(), animaListener)
                })
            }
            VIEW_BOTTOM -> if (bottomView != null) {
                bottomView!!.finish()
                delayTack(Runnable {
                    Instrument.getInstance()
                        .reset(mTargetView, RESET_DURATION.toLong(), animaListener)
                    Instrument.getInstance()
                        .reset(bottomView, RESET_DURATION.toLong(), animaListener)
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
            timer!!.schedule(task, DELAY_DURATION.toLong())
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
        actionListener = null
        timer = null
    }

    private fun clearAnima(v: View?) {
        v?.clearAnimation()
    }


    interface SlidingListener {
        fun onSlidingOffset(v: View, delta: Float)

        fun onSlidingStateChange(v: View, state: Int)

        fun onSlidingChangePointer(v: View, pointerId: Int)
    }

    interface ActionListener {
        fun onLoad()

        fun onRefresh()
    }

}