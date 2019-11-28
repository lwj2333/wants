package com.lwj.wants.recyclerview


import android.content.Context

import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import android.view.ViewGroup


/**
 * @author by  LWJ
 * @date on 2018/10/26
 * @describe 添加描述
 */
class SlidingItemLayout : ViewGroup {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private val TAG = "SlidingItemLayout"

    private fun initView() {

    }

    private var frontView: View? = null
    private var queenView: View? = null
    private var viewCallback: CRecyclerView.RegisterCallBackListener? = null
    fun setViewCallback(listener: CRecyclerView.RegisterCallBackListener) {
        viewCallback = listener
    }

    /**
     * MeasureSpec.UNSPECIFIED   wrap_content                          0
     * MeasureSpec.EXACTLY     match_parent    固定值                 1073741824
     * MeasureSpec.AT_MOST                     wrap_content           -2147483648
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        //   Log.i(TAG, "SlidingItemLayout:$widthSpecMode $widthSpecSize ")
        //   measureChildren(widthMeasureSpec,heightMeasureSpec)
        when (childCount) {
            1 -> {
                frontView = this.getChildAt(0)
                frontView!!.measure(widthMeasureSpec, heightMeasureSpec)
                setMeasuredDimension(frontView!!.measuredWidth, frontView!!.measuredHeight)
            }
            2 -> {
                frontView = this.getChildAt(0)
                measureChild(frontView!!, widthMeasureSpec, heightMeasureSpec)
                queenView = this.getChildAt(1)
                val widthM = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                val heightM = MeasureSpec.makeMeasureSpec(frontView!!.measuredHeight, MeasureSpec.EXACTLY)

                // queenView!!.measure(widthM, heightM)
                measureChild(queenView!!, widthMeasureSpec, heightM)
                //  Log.i(TAG, "SlidingItemLayout:${queenView!!.measuredWidth} ${queenView!!.measuredHeight}")

                setMeasuredDimension(frontView!!.measuredWidth + queenView!!.measuredWidth, frontView!!.measuredHeight)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Log.i(TAG, "SlidingItemLayout:${this.measuredHeight} ${frontView!!.measuredWidth}  ${queenView!!.measuredWidth} ")
        if (frontView != null) {

            frontView!!.layout(0, 0, frontView!!.measuredWidth, frontView!!.measuredHeight)
        }

        if (queenView != null) {
            queenView!!.layout(frontView!!.measuredWidth, 0, frontView!!.measuredWidth + queenView!!.measuredWidth, frontView!!.measuredHeight)
            // queenView!!.layout(0, 0, queenView!!.measuredWidth, queenView!!.measuredHeight)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
          when(ev?.action){
              MotionEvent.ACTION_DOWN->{
                  viewCallback?.onViewListener(this)
              }
          }
        return super.onInterceptTouchEvent(ev)
    }

}