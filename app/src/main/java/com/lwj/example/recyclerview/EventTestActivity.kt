package com.lwj.example.recyclerview

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.lwj.example.BaseActivity
import com.lwj.example.R
import kotlinx.android.synthetic.main.activity_event_test.*

class EventTestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_test)
        view_test.setOnClickListener(this)
        layout_test.setOnClickListener(this)
    }

    /**
     *  0  ACTION_DOWN
     *  1  ACTION_UP
     *  2  ACTION_MOVE
     *  3  ACTION_CANCEL
     */
    private val TAG = "EventTestActivity"
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
     Log.i(TAG,"EventTestActivity_dispatchTouchEvent: ${ev?.action}  ")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
            Log.i(TAG,"EventTestActivity_onTouchEvent: ${event?.action}  ")
        return super.onTouchEvent(event)
    }

}