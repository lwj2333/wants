package com.lwj.wants.recyclerview.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*

import android.support.v4.content.ContextCompat

import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent

import android.view.View
import com.lwj.wants.R
import com.lwj.wants.util.NumberUtils
import java.lang.RuntimeException
import kotlin.math.pow
import kotlin.math.sqrt


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

    private var backgroundPaint: Paint? = null
    private var mBackgroundColor: Int = 0

    private var checkTextPaint: Paint? = null
    private var checkTextColor: Int = 0
    private var showBackgroundPaint: Paint? = null
    private var mShowBackgroundColor: Int = 0
    private var showTextSize: Float = 0f

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
            R.styleable.LetterNavigationView_contentPadding,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
        )
        mBackgroundColor = typedArray.getColor(
            R.styleable.LetterNavigationView_background_color,
            ContextCompat.getColor(context, R.color.dark_green)
        )
        mShowBackgroundColor = typedArray.getColor(
            R.styleable.LetterNavigationView_showBackgroundColor,
            ContextCompat.getColor(context, R.color.letter_show_background)
        )
        showTextSize = typedArray.getDimension(
            R.styleable.LetterNavigationView_showTextSize,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30f, resources.displayMetrics)
        )
        showPadding = typedArray.getDimension(
            R.styleable.LetterNavigationView_showPadding,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
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
        //  arrayContent = arrayOf("A")
        length = getContentLength()
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
        checkTextPaint!!.textSize = showTextSize
        checkTextPaint!!.textAlign = Paint.Align.CENTER
        checkTextPaint!!.color = checkTextColor
        //显示背景画笔
        showBackgroundPaint = Paint()
        showBackgroundPaint!!.isAntiAlias = true
        showBackgroundPaint!!.style = Paint.Style.FILL
        showBackgroundPaint!!.color = mShowBackgroundColor

    }

    private var actionState: Boolean = false    // 是否显示背景圆
    private var isClick: Boolean = false  //是否单击
    private var interval: Float = 0f // 每个字母实际的间隔
    private var heightShould: Float = 0f //一个字母的高度和间隔之和
    private var textX: Float = 0f //字母文本中心x轴
    private var radius: Float = 0f //背景圆的半径

    private var showRadius: Float = 0f //显示背景圆的半径
    private var showPadding: Float = 0f //显示背景圆与字母的偏移
    private var showTextX: Float = 0f //显示文本的字母中心
    private var showTriangleHalf: Float = 0f// 显示背景三角形的斜边的一半
    private var showTriangleX1: Float = 0f// 显示背景三角形第一个x轴
    private var showTriangleX2: Float = 0f// 显示背景三角形第二个x轴
    private var baseLineDistance: Float = 0f //  显示背景文本的基准线偏移量
    private var paddingY: Float = 0f //显示图的半径大于正常情况下heightShould，第一个字母的heightShould
    private val TAG = "LetterNavigationView"

    override fun onDraw(canvas: Canvas?) {


        for (i in 0 until length) {
            //计算Y轴的坐标
            val startY = if (paddingY == 0f) {
                (i + 1) * heightShould + paddingTop
            } else {
                (i) * heightShould + paddingTop + paddingY
            }

            if (actionState && currentIndex > -1 && currentIndex == i) {
                val textY = if (paddingY == 0f) {
                    (i + 1) * heightShould + paddingTop + paddingY - (mRect!!.height() / 2)
                } else {
                    (i) * heightShould + paddingTop + paddingY - (mRect!!.height() / 2)
                }
                canvas?.drawCircle(textX, textY, radius, backgroundPaint!!)
                checkTextPaint!!.textSize = mTextSize
                canvas?.drawText(arrayContent!![i], textX, startY, checkTextPaint!!)

                checkTextPaint!!.textSize = showTextSize
                canvas?.drawCircle(showTextX, textY, showRadius, showBackgroundPaint!!)
                //   showBackgroundPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
                val p = Path()
                p.moveTo(showTriangleX1, textY - showTriangleHalf)
                p.lineTo(showTriangleX1, textY + showTriangleHalf)
                p.lineTo(showTriangleX2, textY)
                p.close()
                canvas?.drawPath(p, showBackgroundPaint!!)
                //  showBackgroundPaint!!.xfermode =null
                canvas?.drawText(
                    arrayContent!![i],
                    showTextX,
                    textY + baseLineDistance,
                    checkTextPaint!!
                )
            } else {
                if (!actionState && isClick && currentIndex > -1 && currentIndex == i) {
                    val textY = if (paddingY == 0f) {
                        (currentIndex + 1) * heightShould + paddingTop + paddingY - (mRect!!.height() / 2)
                    } else {
                        (currentIndex) * heightShould + paddingTop + paddingY - (mRect!!.height() / 2)
                    }
                    canvas?.drawCircle(textX, textY, radius, backgroundPaint!!)
                    checkTextPaint!!.textSize = mTextSize
                    canvas?.drawText(arrayContent!![currentIndex], textX, startY, checkTextPaint!!)
                    continue
                }
                //绘制文字
                canvas?.drawText(arrayContent!![i], textX, startY, textPaint!!)
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val eventY = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x > showArea) {
                    actionState = true
                    isClick = true
                    scrollCount(eventY!!)
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {

                isClick = false

                scrollCount(eventY!!)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                actionState = false
                currentLetter = null
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        interval =
            (h - paddingTop - paddingBottom - length * mRect!!.height()).toFloat() / (length + 1)
        heightShould = interval + mRect!!.height()

        val textY = 1 * heightShould + paddingTop - (mRect!!.height() / 2)
        val showDistance = showRadius + paddingTop
        if (showDistance > textY) {
            paddingY = showRadius + (mRect!!.height() / 2)
            interval =
                (h - paddingTop - paddingBottom - paddingY * 2 - (length - 2) * mRect!!.height()) / (length - 1)
            heightShould = interval + mRect!!.height()

        }
        //字母文本的中心x轴
        textX = paddingLeft + showArea + mContentDiv + mRect!!.width() / 2
        //背景圆的半径
        radius = mRect!!.height() - (mContentDiv / 2)
        //显示文本的中心x轴
        showTextX = paddingLeft + showRadius
        showTriangleHalf = sqrt(showRadius.pow(2) * 2) / 2

        showTriangleX1 = showTextX + showTriangleHalf
        showTriangleX2 = showTextX + showTriangleHalf * 2

        val fontMetrics = checkTextPaint!!.fontMetrics
        baseLineDistance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
    }

    private var showArea: Float = 0f

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

        mRect = measureTextSize(textPaint!!)
        val showRect = measureTextSize(checkTextPaint!!)
        showRadius = (showRect.height()).toFloat()

        //内容的最小宽度
        val contentWidth = mRect!!.width() + mContentDiv * 2
        //内容的最小高度
        val contentHeight = mRect!!.height() * length + mContentDiv * (length + 1)

        //显示区域的宽度
        showArea = showRadius * 3 + showPadding

        when (widthMode) {
            MeasureSpec.AT_MOST -> {
                mWidth = (contentWidth + showArea + paddingLeft + paddingRight).toInt()
            }
            MeasureSpec.EXACTLY -> {
                if (mWidth < contentWidth) {
                    mWidth = (contentWidth + showArea + paddingLeft + paddingRight).toInt()
                }
            }
            else -> {
            }
        }
        when (heightMode) {
            MeasureSpec.AT_MOST -> {
                val showDiameter = showRadius * 2
                mHeight = if (contentHeight < showDiameter) {
                    (showDiameter + paddingTop + paddingBottom).toInt()
                } else {
                    (contentHeight + paddingTop + paddingBottom).toInt()
                }

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

    private var currentIndex: Int = -1
    private var currentLetter: String? = null
    /**
     * 滑动计算
     */
    private fun scrollCount(eventY: Float) {

        val index: Int =
            NumberUtils.round(((eventY - paddingTop) / heightShould).toDouble()).toInt() - 1
        if (index in 0 until length) {
            val letter = arrayContent!![index]
            if (!currentLetter.equals(letter)) {
                currentLetter = letter
                currentIndex = index
                invalidate()
                mNavigationChangeListener?.onChange(letter, index)
            }
        }
    }

    /**
     * 测量文字的尺寸
     */
    fun measureTextSize(paint: Paint): Rect {
        val mRect = Rect()
        paint.getTextBounds("田", 0, 1, mRect)
        return mRect
    }

    private var length = 0 //字母长度
    fun getContentLength(): Int {
        if (arrayContent != null) {
            return arrayContent!!.size
        }
        return 0
    }

    fun setLetterArray(array: Array<String>) {
        if (array.isEmpty()) {
            throw RuntimeException("数组长度必须大于0")
        }

        arrayContent = array
        length = getContentLength()
        requestLayout()
    }

    private var mNavigationChangeListener: OnNavigationChangeListener? = null
    fun setNavigationChangeListener(listener: OnNavigationChangeListener) {
        mNavigationChangeListener = listener
    }

    interface OnNavigationChangeListener {
        fun onChange(letter: String, position: Int)
    }
}