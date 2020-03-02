package com.lwj.wants.recyclerview.widgets

import android.content.Context

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.support.v4.content.ContextCompat

import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent

import android.view.View
import com.lwj.wants.R


class LetterNavigationView : View {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
        initData()
    }


    private var textPaint: Paint? = null
    private var mTextSize: Float = 10f
    private var mTextColor: Int = 0
    private var mContentDiv: Float = 0f
    private var mRect: Rect? = null
    private val mRectF = RectF()
    private var backgroundPaint: Paint? = null
    private var mBackgroundColor: Int = 0

    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.LetterNavigationView)

        mTextSize =
            typedArray.getDimension(
                R.styleable.LetterNavigationView_android_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
            )
        mTextColor = typedArray.getColor(
            R.styleable.LetterNavigationView_android_textColor,
            ContextCompat.getColor(context, R.color.black)
        )
        mContentDiv = typedArray.getDimension(
            R.styleable.LetterNavigationView_content_div, 16f
        )
        mBackgroundColor = typedArray.getColor(
            R.styleable.LetterNavigationView_background_color,
            ContextCompat.getColor(context, R.color.green)
        )
        typedArray?.recycle()
    }

    private var arrayContent: Array<String>? = null

    private fun initData() {
        arrayContent = arrayOf(
            "搜",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z"
        )
       // arrayContent = arrayOf("日", "田")
        Log.i(TAG, "LetterNavigationView_initData: $mTextSize  $mContentDiv ")
        //文字画笔
        textPaint = Paint()
        textPaint!!.isAntiAlias = true
        textPaint!!.textSize = mTextSize
        textPaint!!.textAlign = Paint.Align.CENTER
        textPaint!!.color = mTextColor
        //背景画笔
        backgroundPaint = Paint()
        backgroundPaint!!.isAntiAlias = true
        backgroundPaint!!.style = Paint.Style.FILL
        backgroundPaint!!.color = mBackgroundColor

    }

    private var actionState = true
    private val TAG = "LetterNavigationView"
    override fun onDraw(canvas: Canvas?) {


        //绘制文本
        val textX: Float = (width / 2).toFloat()

        //X轴坐标
        val length = getContentLength()
        //Y轴坐标 （这里在测量的时候多加入了两个间隔高度要减去，同时还有Padding值
        val heightShould: Float =
            (height - mContentDiv - paddingTop - paddingBottom) / length
        Log.i(
            TAG,
            "LetterNavigationView_onDraw: $height $paddingTop $paddingBottom  $heightShould "
        )
        //背景圆的半径
        val radius :Float = heightShould/2
        for (i in 0 until length) {
            //计算Y轴的坐标
            val startY = (i + 1) * heightShould + paddingTop
            Log.i(TAG, "LetterNavigationView_onDraw: $startY  ")
            if (actionState&&i==1) {
                val ds = i * heightShould + paddingTop + mContentDiv + (mRect!!.height() / 2)

                canvas?.drawCircle(textX, ds, radius, backgroundPaint!!)
                Log.i(TAG, "LetterNavigationView_onDraw:元  $ds  $heightShould $radius ")
            }
            //绘制文字
            canvas?.drawText(arrayContent!![i], textX, startY, textPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val eventY = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                actionState = true
                invalidate()
                scrollCount(eventY!!)
            }
            MotionEvent.ACTION_MOVE -> {
                scrollCount(eventY!!)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                actionState = false
                invalidate()

            }
        }
        return true
    }

    /**
     *     不管设置的是真实尺寸或者是包裹内容，都会以内容的最小尺寸为
     *     基础，如果设置的控件尺寸大于我们内容的最小尺寸，就使用控件
     *     尺寸，反之使用内容的最小尺寸
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //获取控件的尺寸
        var mWidth = MeasureSpec.getSize(widthMeasureSpec)
        var mHeight = MeasureSpec.getSize(heightMeasureSpec)
        val length = getContentLength()
        mRect = measureTextSize()
        //内容的最小宽度
        val contentWidth = mRect!!.width() + mContentDiv * 2
        //内容的最小高度
        val contentHeight = mRect!!.height() * length + mContentDiv * (length + 1)

        when (widthMode) {
            MeasureSpec.AT_MOST -> {
                mWidth = (contentWidth + paddingLeft + paddingRight).toInt()
            }
            MeasureSpec.EXACTLY -> {
                if (mWidth < contentWidth) {
                    mWidth = (contentWidth + paddingLeft + paddingRight).toInt()
                }
            }
            else -> {
            }
        }
        when (heightMode) {
            MeasureSpec.AT_MOST -> {
                mHeight = (contentHeight + paddingTop + paddingBottom).toInt()
            }
            MeasureSpec.EXACTLY -> {
                if (mHeight < contentHeight) {
                    mHeight = (contentHeight + paddingTop + paddingBottom).toInt()
                }
            }
            else -> {
            }
        }
        setMeasuredDimension(mWidth, mHeight)
        Log.i(
            TAG,
            "LetterNavigationView_onDraw: $mWidth  $mHeight  $mContentDiv  ${mRect!!.height()}  $contentHeight"
        )
    }

    private var currentLetter: String? = null
    /**
     * 滑动计算
     */
    private fun scrollCount(eventY: Float) {
        val mRect = measureTextSize()
        val index: Int =
            (((eventY - paddingTop - paddingBottom - mContentDiv * 2) / (mRect.height() + mContentDiv)).toInt())
        if (index >= 0 && index < getContentLength()) {
            val letter = arrayContent?.get(index)
            Log.i(TAG, "LetterNavigationView_scrollCount: $letter  ")
            if (!currentLetter.equals(letter)) {
                currentLetter = letter
                //TODO 触摸回调
                Log.i(TAG, "LetterNavigationView_scrollCount: 唯有 $letter  ")
            }
        } else {
            Log.i(TAG, "LetterNavigationView_scrollCount: $ 超出  ")
        }

    }

    /**
     * 测量文字的尺寸
     */
    fun measureTextSize(): Rect {
        val mRect = Rect()
        if (textPaint != null) {
            textPaint!!.getTextBounds("田", 0, 1, mRect)
        }
        return mRect
    }

    fun getContentLength(): Int {
        if (arrayContent != null) {
            return arrayContent!!.size
        }
        return 0
    }
}