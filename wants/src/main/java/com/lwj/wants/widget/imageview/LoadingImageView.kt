package com.lwj.wants.widget.imageview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.*
import android.widget.ImageView


class LoadingImageView : ImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    //补间动画
    fun startTweenAutoRotate(duration: Long, repeat: Boolean = false) {
        val anim = RotateAnimation(0f, 360f, centreW, centreH)
        anim.duration = duration
        anim.fillAfter = true // 设置保持动画最后的状态
        anim.interpolator = LinearInterpolator() // 设置插入器
        if (repeat) {
            anim.repeatMode = Animation.RESTART    // 重复模式   重新开始
            anim.repeatCount = Animation.INFINITE  // 重复次数   无限循环
        }
        this.startAnimation(anim)
    }

    private var anim: ObjectAnimator? = null
    fun startViewAutoRotate(duration: Long = 1000L, repeat: Boolean = false) {
        anim = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
        anim?.duration = duration
        anim?.interpolator = LinearInterpolator() // 设置插入器
        if (repeat) {
         //   anim?.repeatMode = ValueAnimator.RESTART    // 重复模式   重新开始
            anim?.repeatCount = ValueAnimator.INFINITE  // 重复次数   无限循环
        }
        anim?.start()
    }

    fun endAnimator() {
     anim?.end()
    }

    fun cancel(){
        anim?.cancel()
    }
    fun reverse(){
        anim?.reverse()
    }

    private val TAG = "LoadingImageView"
    fun setProgressRotate(progress: Float) {
        val rotate = 360 * progress
        this.rotation = rotate
    }

    private var centreW: Float = 0f
    private var centreH: Float = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centreH = (h / 2).toFloat()
        centreW = (w / 2).toFloat()
    }


}