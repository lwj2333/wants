package com.lwj.wants.widget.textview

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.TextView
import com.lwj.wants.R


class TextViewGradient : TextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.TextViewGradient)
        if (typedArray != null) {
            colorStart = typedArray.getColor(R.styleable.TextViewGradient_TextViewGradient_colorStart,
                    ContextCompat.getColor(context, R.color.TextViewGradient_start))
            colorEnd = typedArray.getColor(R.styleable.TextViewGradient_TextViewGradient_colorEnd,
                    ContextCompat.getColor(context, R.color.TextViewGradient_end))
            round = typedArray.getDimension(R.styleable.TextViewGradient_TextViewGradient_round, 0f)
        }
        typedArray?.recycle()
        initPaint()
    }

    private var mPaint: Paint? = null
    private fun initPaint() {
        mPaint = Paint()
        //设置抗锯齿
        mPaint?.isAntiAlias = true
        //设置防抖动
        mPaint?.isDither = true
        mPaint?.style = Paint.Style.FILL
    }



    private var mBackGroundRect: RectF? = null
    private var backGradient: LinearGradient? = null
    private var colorStart = -1
    private var colorEnd = -1
    private var round = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (round==0f){
            round = (h/2).toFloat()
        }
        mBackGroundRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
        backGradient = LinearGradient(0f, 0f, w.toFloat(), 0f,
                colorStart, colorEnd, Shader.TileMode.CLAMP)

    }

    override fun onDraw(canvas: Canvas?) {


        mPaint?.shader = backGradient
        if (mBackGroundRect != null) {
            canvas?.drawRoundRect(mBackGroundRect!!, round, round, mPaint!!)
        }

        super.onDraw(canvas)
    }
}