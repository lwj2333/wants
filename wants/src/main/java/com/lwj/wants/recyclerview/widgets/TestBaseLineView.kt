package com.lwj.wants.recyclerview.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.lwj.wants.util.DensityUtil
import kotlin.math.pow
import kotlin.math.sqrt

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
        // super.onDraw(canvas)
//        val x: Float = DensityUtil.dip2px(context, 50f).toFloat()
//        val y: Float = DensityUtil.dip2px(context, 50f).toFloat()
//        mPaint!!.setStyle(Paint.Style.FILL)
//        mPaint!!.color = Color.parseColor("#99EC4541")
//
//        //    canvas?.drawCircle(200f,200f,50f,mPaint!!)
//        canvas?.drawCircle(300f,200f,50f,mPaint!!)
//        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
//        mPaint!!.color = Color.parseColor("#998EDB2A")
//        canvas?.drawCircle(200f,200f,150f,mPaint!!)
   // canvas?.drawCircle(300f,200f, 150f,mPaint!!)

     //   loveHeart(canvas)
        guaguaka(canvas)
    }

    private val TAG = "TestBaseLineView"
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
    private fun guaguaka(canvas: Canvas?){
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.alpha =0
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 50f
        paint.strokeJoin = Paint.Join.ROUND //设置连接样式
        paint.strokeCap = Paint.Cap.ROUND //设置画笔的线帽样式

    }
    private fun loveHeart(canvas: Canvas?) {
        val x1 = 100f
        val y1 = 100f
        val x2 = 200f
        val y2 = 100f
        canvas?.drawCircle(x1, y1, 50f, mPaint!!)
        canvas?.drawCircle(x2, y2, 50f, mPaint!!)
        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas?.drawCircle(x2, y2, 150f, mPaint!!)
      //  canvas?.drawCircle(x1, y1, 150f, mPaint!!)


    }
}