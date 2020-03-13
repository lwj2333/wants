package com.lwj.wants.widget.imageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

import android.view.MotionEvent
import com.lwj.wants.R


class GuaGuaKaView : AppCompatImageView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GuaGuaKaView)
        shade = typedArray.getResourceId(R.styleable.GuaGuaKaView_Shade_bg, 0)
        typedArray.recycle()
        initData()
    }

    private fun initData() {
        mSwipePaintWidth = DEFAULT_SWIPE_PAINT_WIDTH
        mSwipeCompletePercentage = DEFAULT_SWIPE_COMPLETE_PERCENTAGE
        paint = Paint()
        paint!!.style = Paint.Style.STROKE
        paint!!.alpha = 0
        paint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        paint!!.strokeWidth = mSwipePaintWidth
        paint!!.strokeJoin = Paint.Join.ROUND //设置连接样式
        paint!!.strokeCap = Paint.Cap.ROUND //设置画笔的线帽样式
    }

    private var mCanvas: Canvas? = null
    private var paint: Paint? = null
    private var path: Path? = null
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mCompleted) {
            mOnCompleteListener?.complete()
        }
        //判断是否完成，如果完成了就不绘制遮盖层
        if (!mCompleted) {
//            canvas?.drawBitmap(backBitmap!!, 0f, 0f, null)   //画目标图像底片
            canvas?.drawBitmap(canvasBitmap!!, 0f, 0f, null)  //画遮罩
        }

    }

    private var mLastX = 0f
    private var mLastY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x
                mLastY = event.y
                //   path!!.reset()
                path!!.moveTo(mLastX, mLastY)
            }
            MotionEvent.ACTION_MOVE -> {

                val dx = Math.abs(x - mLastX)
                val dy = Math.abs(y - mLastY)
                if (dx > 3 || dy > 3) {
                    path!!.lineTo(event.x, event.y)
                    if (null != mOnScratchListener) mOnScratchListener!!.onScratch()
                }
                mLastX = event.x
                mLastY = event.y
                // post(mTask)
                detection()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        drawingPath()

        return true
    }

    private fun drawingPath() {
        mCanvas!!.drawPath(path!!, paint!!)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (shade != 0) {
            fgBitmap = BitmapFactory.decodeResource(resources, shade)
        }
        //源图像
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(canvasBitmap!!)

        initShade()
    }


    private var fgBitmap: Bitmap? = null
    private var canvasBitmap: Bitmap? = null

    private var shade: Int = 0
    /**
     * 初始化遮罩层
     */
    private fun initShade() {
        path = Path()

        if (fgBitmap == null) {
            //源图像 画一个灰色的遮罩
            mCanvas!!.drawColor(Color.GRAY)
        } else {
            val rect =  Rect(0, 0, width, height)
            //源图像 画一个灰色的指定遮罩
            mCanvas!!.drawBitmap(fgBitmap!!, null,rect, null)
        }
        // -7829368  Color.GRAY
        // 0 透明
    }



    val DEFAULT_SWIPE_PAINT_WIDTH = 40f
    val DEFAULT_SWIPE_COMPLETE_PERCENTAGE = 30
    private var mSwipeCompletePercentage = 0
    private var mSwipePaintWidth = 0f
    private var mOnCompleteListener: OnCompleteListener? = null
    private var mOnScratchListener: OnScratchListener? = null
    //判断遮盖层区域是否达到消除的比例
    @Volatile
    private var mCompleted = false
    private var mThread: Thread? = null
    /**
     * 起一个线程来计算已经扫的面积及占总区域的比例
     * 根据区域来判断是否完成
     */
    private fun detection() {
        if (mThread == null) {

            val totalArea = width * height.toFloat()
            val bitmap = canvasBitmap
            val mPixels = IntArray(width * height)
            mThread = Thread(Runnable {
                while (!mCompleted) {

                    //获取bitmap的所有像素信息
                    //getPixels()函数把一张图片，从指定的偏移位置（offset），
                    // 指定的位置（x,y）截取指定的宽高（width,height ），
                    // 把所得图像的每个像素颜色转为int值，存入pixels
                    //stride 参数指定在行之间跳过的像素的数目。图片是二维的，存入一个一维数组中，
                    // 那么就需要这个参数来指定多少个像素换一行
                    bitmap!!.getPixels(mPixels, 0, width, 0, 0, width, height)
                    var wipeArea = 0f
                    for (i in 0 until width)
                        for (j in 0 until height) {
                            val index = i + j * width
                            //  Log.i(TAG,"Svs_: $index  $i $j  ${mPixels[index]} ")
                            if (mPixels[index] == 0) {
                                wipeArea++
                            }
                        }
                    // Log.i(TAG,"GuaGuaKaView_detection: ${(wipeArea * 100 / totalArea).toInt()} $wipeArea  $totalArea  ")
                    //计算已扫区域所占的比例
                    if (wipeArea > 0 && totalArea > 0) {
                        val percent = (wipeArea * 100 / totalArea).toInt()
                        if (percent > mSwipeCompletePercentage) { //清除图层区域
                            mCompleted = true
                            postInvalidate()
                        }
                    }
                }
            })
            mThread!!.start()
        }
    }

    /**
     * 重置刮刮卡 并可重新指定新的底层图片
     */
    fun rest(drawable: Drawable?) {
        mCompleted = false
        mThread = null
        initShade()
        if(drawable!=null){
        setImageDrawable(drawable)
        }else{
            invalidate()
        }
    }

    /**
     * 释放线程
     */
    fun recycle(){
        mCompleted=true
        mThread = null
    }
    /**
     * 设置刮卡完成比例
     *
     * @param per
     */
    fun setCompletePercentage(per: Int) {
        mSwipeCompletePercentage = per
    }

    /**
     * 设置刮卡完成监听
     *
     * @param mOnCompleteListener
     */
    fun setOnCompleteListener(mOnCompleteListener: OnCompleteListener?) {
        this.mOnCompleteListener = mOnCompleteListener
    }

    /**
     * 设置刮卡开始监听
     *
     * @param mOnScratchListener
     */
    fun setOnScratchListener(mOnScratchListener: OnScratchListener?) {
        this.mOnScratchListener = mOnScratchListener
    }

    /**
     * 刮刮卡开始刮时回调
     */
    interface OnScratchListener {
        fun onScratch()
    }

    /**
     * 刮刮卡刮完之后的回调接口
     */
    interface OnCompleteListener {
        fun complete()
    }
}