package com.lwj.example.recyclerview.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class TestEventView :View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    private val TAG ="EventTestActivity"
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
          Log.i(TAG,"TestEventView_dispatchTouchEvent: ${event?.action}  ")
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
          Log.i(TAG,"TestEventView_onTouchEvent: ${event?.action}  ")
        return super.onTouchEvent(event)
    }
}