package com.lwj.wants.recyclerview.animation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.lwj.wants.recyclerview.SlidingLayout

class AnimationSlidingLayout : SlidingLayout {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var topview: AnimationTop? = null
    override fun initView() {
        topview = getTopView() as AnimationTop
        setmSlidingMode(SlidingLayout.SLIDING_MODE_TOP)
        setSlidingListener(object : SlidingListener {
            override fun onSlidingOffset(v: View, delta: Float) {
                Log.i(TAG, "AnimationSlidingActivity_onSlidingOffset: $delta  ")
                if (delta >= refreshStateY) {
                    topview?.action()
                } else {
                    topview?.hint()
                }
            }

            override fun onSlidingStateChange(v: View, state: Int) {
                Log.i(TAG, "AnimationSlidingActivity_onSlidingOffset: $state  ")
                when(state){
                    SlidingLayout.STATE_TOP->{topview?.finish()}
                }
            }

            override fun onSlidingChangePointer(v: View, pointerId: Int) {

            }
        })
    }

    private var actionListener: OnSlidingResultListener? = null
    fun setSlidingResultListener(listener: OnSlidingResultListener){
        actionListener =listener
    }
    private val TAG = "AnimationSlidingLayout"
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                Log.i(TAG, "AnimationSlidingLayout_onTouchEvent: $ lalllllalla ")
                if ( getSlidingListener()!= null) {
                    getSlidingListener()!!.onSlidingStateChange(this, SlidingLayout.STATE_UP)
                }
                if (getSlidingDistance()>refreshStateY){
                    topview?.run()
                    actionListener?.onRefresh()
                }
                slidingToRefresh()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    interface OnSlidingResultListener {
       // fun onLoad()
        fun onRefresh()
    }
}