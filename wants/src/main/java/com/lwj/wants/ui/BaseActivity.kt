package com.lwj.wants.ui


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private var inputMethodManager: InputMethodManager? = null
    private val isKeyboardActive: Boolean
        get() = if (inputMethodManager != null) {
            this.inputMethodManager!!.isActive
        } else {
            false
        }
    fun showSoftKeyboard(view: View?) {
        if (inputMethodManager != null && view != null)
            inputMethodManager!!.showSoftInput(view, 0)
    }

    fun hideSoftKeyboard(view: View?) {
        if (inputMethodManager != null && view != null)
            inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && isKeyboardActive) {
            hideSoftKeyboard(window.currentFocus)
        }
        return super.dispatchTouchEvent(event)
    }
}