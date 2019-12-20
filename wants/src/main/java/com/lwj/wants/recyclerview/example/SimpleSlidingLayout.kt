package com.lwj.wants.recyclerview.example

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.lwj.wants.recyclerview.SlidingLayout

class SimpleSlidingLayout : SlidingLayout {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var topView: SimpleTopView? = null

    override fun initView() {
        topView = getTopView() as SimpleTopView

        setmSlidingMode(SlidingLayout.SLIDING_MODE_TOP)
        setSlidingListener(object : SlidingListener {
            override fun onSlidingOffset(v: View, delta: Float) {

                when {
                    //下拉
                    getSlidingDistance() > 0 -> {
                        if (getRefreshStats() != Stats.RUN) {
                            when {
                                delta >= refreshStateY && getRefreshStats() != Stats.READY -> {
                                    setRefreshStats(Stats.READY)
                                    topView?.action()
                                }
                                delta < refreshStateY && getRefreshStats() != Stats.DEFAULT -> {
                                    setRefreshStats(Stats.DEFAULT)
                                    topView?.hint()
                                }
                            }
                        }
                    }
                    //上推
//                    getSlidingDistance() < 0 -> {
//
//                    }
                }
            }

            override fun onSlidingStateChange(v: View, state: Int) {
                when (state) {
                    SlidingLayout.STATE_FINISH_TOP -> {
                        topView?.finish()
                        setRefreshStats(Stats.COMPLETE)
                    }
//                    SlidingLayout.STATE_FINISH_DOWN -> {
//                        bottomView?.finish()
//                        setRefreshStats(Stats.COMPLETE)
//                    }
                }
            }

            override fun onSlidingChangePointer(v: View, pointerId: Int) {

            }
        })
    }

    private var actionListener: OnSlidingResultListener? = null
    fun setSlidingResultListener(listener: OnSlidingResultListener) {
        actionListener = listener
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN->{

            }
            MotionEvent.ACTION_MOVE -> {

                if (getSlidingDistance() != 0f && getRefreshStats() == Stats.COMPLETE) {
                    return true
                }

            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

                if (getSlidingListener() != null) {
                    getSlidingListener()!!.onSlidingStateChange(this, SlidingLayout.STATE_UP)
                }

                when {
                    getSlidingDistance() > refreshStateY && getRefreshStats() != Stats.RUN -> {
                        setRefreshStats(Stats.RUN)
                        topView?.run()
                        actionListener?.onRefresh()
                    }
//                    getSlidingDistance() < -loadStateY -> {
//                        setRefreshStats(Stats.RUN)
//                        bottomView?.run()
//                        actionListener?.onLoad()
//                    }
                }
                slidingToRefresh()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    interface OnSlidingResultListener {
        fun onLoad()
        fun onRefresh()
    }


}