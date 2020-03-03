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
    private var mContentDiv: Float = 0f //字母间隔
    private var mRect: Rect? = null
    private val mRectF = RectF()
    private var backgroundPaint: Paint? = null
    private var mBackgroundColor: Int = 0

    private var checkTextPaint: Paint? = null
   private var checkTextColor:Int =0

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
        checkTextColor = typedArray.getColor(
            R.styleable.LetterNavigationView_textColor_checked,
            ContextCompat.getColor(context, R.color.white)
        )
        mContentDiv = typedArray.getDimension(
            R.styleable.LetterNavigationView_content_div,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
        )
        mBackgroundColor = typedArray.getColor(
            R.styleable.LetterNavigationView_background_color,
            ContextCompat.getColor(context, R.color.dark_green)
        )
        typedArray?.recycle()
    }

    private var arrayContent: Array<String>? = null

    private fun initData() {
        arrayContent = arrayOf(
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
        // arrayContent = arrayOf("a", "b","c")
        length = getContentLength()
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
        //选中文字画笔
        checkTextPaint = Paint()
        checkTextPaint!!.isAntiAlias = true
        checkTextPaint!!.textSize = mTextSize
        checkTextPaint!!.textAlign = Paint.Align.CENTER
        checkTextPaint!!.color = checkTextColor
    }

    private var actionState = false
    private var interval: Float = 0f // 每个字母实际的间隔
    private var heightShould: Float = 0f //一个字母的高度和间隔之和
    private var textX :Float = 0f //文本中心x轴
    private var radius: Float=0f //背景圆的半径
    private val TAG = "LetterNavigationView"
    override fun onDraw(canvas: Canvas?) {


        for (i in 0 until length) {
            //计算Y轴的坐标
            val startY = (i + 1) * heightShould + paddingTop
            Log.i(TAG, "LetterNavigationView_onDraw: $startY  ")
            if (actionState&&currentIndex>-1&&currentIndex==i) {
                val textY = (i + 1) * heightShould + paddingTop - (mRect!!.height() / 2)
                canvas?.drawCircle(textX, textY, radius, backgroundPaint!!)
                canvas?.drawText(arrayContent!![i], textX, startY, checkTextPaint!!)
            }else{
                //绘制文字
                canvas?.drawText(arrayContent!![i], textX, startY, textPaint!!)
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val eventY = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                actionState = true
                scrollCount(eventY!!)
            }
            MotionEvent.ACTION_MOVE -> {
                scrollCount(eventY!!)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                actionState = false
                currentLetter = null
                currentIndex = -1
                invalidate()

            }
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        interval = (h-paddingTop-paddingBottom - length * mRect!!.height()).toFloat() / (length + 1)
        heightShould = interval+mRect!!.height()
        textX = (width / 2).toFloat()
        //背景圆的半径
        radius = mRect!!.height()-(mContentDiv/2)
        Log.i(TAG, "LetterNavigationView_onSizeChanged: $interval  $heightShould")
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
    }
    private var currentIndex :Int =-1
    private var currentLetter: String? = null
    /**
     * 滑动计算
     */
    private fun scrollCount(eventY: Float) {

        val index: Int = ((eventY - paddingTop) /heightShould).toInt()
        if (index in 0 until length) {
            val letter = arrayContent!![index]
            Log.i(TAG, "LetterNavigationView_scrollCount: $letter  ")
            if (!currentLetter.equals(letter)) {
                currentLetter = letter
                currentIndex = index
                invalidate()
                mNavigationChangeListener?.onChange(letter,index)
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
    private var length = 0 //字母长度
    fun getContentLength(): Int {
        if (arrayContent != null) {
            return arrayContent!!.size
        }
        return 0
    }
    fun setLetterArray(array:Array<String>){
        arrayContent = array
        length = getContentLength()
    }
    private var mNavigationChangeListener:OnNavigationChangeListener ?=null
    fun setNavigationChangeListener(listener:OnNavigationChangeListener){
        mNavigationChangeListener = listener
    }

    interface  OnNavigationChangeListener{
        fun onChange(letter:String,position:Int)
    }
}