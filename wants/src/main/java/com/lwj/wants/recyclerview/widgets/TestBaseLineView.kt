package com.lwj.wants.recyclerview.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.lwj.wants.util.DensityUtil

class TestBaseLineView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initData()
    }

    private var mPaint: Paint? = null
    private fun initData() {
        mPaint = Paint()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val x:Float = DensityUtil.dip2px(context,50f).toFloat()
        val y:Float = DensityUtil.dip2px(context,50f).toFloat()
        mPaint!!.setStyle(Paint.Style.FILL)
       mPaint!!.color = Color.parseColor("#99EC4541")

        canvas?.drawRect(0f,0f,200f,200f,mPaint!!)

        mPaint!!.color = Color.parseColor("#998EDB2A")
        canvas?.drawRect(100f,100f,300f,300f,mPaint!!)

    }
    private val TAG ="TestBaseLineView"
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}