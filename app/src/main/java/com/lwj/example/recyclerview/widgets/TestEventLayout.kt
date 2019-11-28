package com.lwj.example.recyclerview.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

class TestEventLayout :LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    private val TAG ="EventTestActivity"
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
          Log.i(TAG,"TestEventLayout_dispatchTouchEvent: ${ev?.action}  ")
        return super.dispatchTouchEvent(ev)
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
          Log.i(TAG,"TestEventLayout_onInterceptTouchEvent: ${ev?.action}  ")
        if (ev?.action==1){
            return true
        }
        return super.onInterceptTouchEvent(ev)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
          Log.i(TAG,"TestEventLayout_onTouchEvent: ${event?.action}  ")
        return super.onTouchEvent(event)
    }
}