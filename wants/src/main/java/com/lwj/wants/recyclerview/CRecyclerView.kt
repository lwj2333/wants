package com.lwj.wants.recyclerview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import com.lwj.wants.recyclerview.util.SlidingLinearLayoutManager

class CRecyclerView : RecyclerView {


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    private var targetView: ViewGroup? = null
    private var hideWidth: Int = 0
    private var isOpen: Boolean = false
    private val TAG = "CRecyclerView"


    fun registerView(v: ViewGroup) {

        if (v != targetView && isOpen) {
            reset()
            mode = wild
            this.targetView = null
            hideWidth = 0
            return
        }
        if (v.childCount > 1) {
            this.targetView = v
            hideWidth = v.getChildAt(1).width
        }
    }

    fun releaseView() {
        this.targetView = null
        hideWidth = 0
        isOpen = false
    }


    private val vertical: Int = 2
    private val landscape: Int = 1
    private val wild: Int = 3
    private var mode: Int = 0
    private var manager: SlidingLinearLayoutManager? = null
    fun setManager(manager: SlidingLinearLayoutManager) {
        this.manager = manager
    }

    private var downX: Float = 0f
    private var downY: Float = 0f
    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = e.x
                downY = e.y
                manager?.setScrollEnabled(false)
                Log.i(TAG, "CRecyclerView: $  按下")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, "CRecyclerView: $  移动")
                when (mode) {
                    landscape -> {
                    }
                    vertical -> {
                    }
                    0 -> {
                        val lengthX = Math.abs(e.x - downX)
                        if (lengthX >= 21) {
                            mode = landscape
                            return true
                        }
                        val lengthY = Math.abs(e.y - downY)
                        if (lengthY >= 21 && mode == 0) {
                            mode = vertical
                            manager?.setScrollEnabled(true)
                            return true
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                downX = 0f
                downY = 0f
                mode = 0
            }
        }

        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "CRecyclerView: $ 消费 按下  ")
                if (mode == wild) {
                    return super.onTouchEvent(e)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, "CRecyclerView: $ 消费 移动  ")
                when (mode) {
                    landscape -> {
                        Log.i(TAG, "CRecyclerView: $ 消费 横  ")
                        val distanceX = e.x - downX
                        downX = e.x
                        sliding(distanceX)

                    }
                    vertical -> {
                        Log.i(TAG, "CRecyclerView: $  竖  ")
                    }
                    0 -> {
                        val lengthX = Math.abs(e.x - downX)
                        if (lengthX >= 21) {
                            mode = landscape
                        }
                        val lengthY = Math.abs(e.y - downY)
                        if (lengthY >= 21 && mode == 0) {
                            mode = vertical
                            manager?.setScrollEnabled(true)
                        }
                    }
                }

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                downX = 0f
                downY = 0f
                mode = 0
                if (targetView != null) {
                    if (targetView!!.translationX < 0 && Math.abs(targetView!!.translationX) >= (hideWidth / 2)) {
                        autoPopup()
                    } else {
                        reset()
                    }
                }

            }
        }
        return super.onTouchEvent(e)
    }

    private fun sliding(distanceX: Float) {
        if (targetView != null && isSliding) {

            val distance = targetView!!.translationX + distanceX
            if (distance < 0 && Math.abs(distance) > hideWidth) {
                Log.i(TAG, "CRecyclerView: $ 错误 ")
                targetView!!.translationX = -hideWidth.toFloat()
                isOpen = true
            } else {
                targetView!!.translationX += distanceX
            }
        }
    }

    private var isSliding: Boolean = true
    private fun autoPopup(duration: Long = 200) {
        if (targetView == null) {
            return
        }
        targetView?.clearAnimation()
        val animator = ObjectAnimator.ofFloat(targetView!!, "translationX", -hideWidth.toFloat())
        animator.duration = duration
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {
                isSliding = true
            }

            override fun onAnimationStart(animation: Animator?) {
                isSliding = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                isSliding = true
                isOpen = true
            }
        })
        animator.start()
    }

    private fun reset(duration: Long = 200) {
        if (targetView == null) {
            return
        }
        targetView?.clearAnimation()
        Log.i(TAG, "CRecyclerView: $  还原")
        val animator = ObjectAnimator.ofFloat(targetView!!, "translationX", 0f)
        animator.duration = duration
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {
                isSliding = true
            }

            override fun onAnimationStart(animation: Animator?) {
                isSliding = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                isSliding = true
                isOpen = false
            }
        })
        animator.start()
    }

    interface RegisterCallBackListener {
        fun onViewListener(v: ViewGroup)
    }
}