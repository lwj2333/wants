package com.lwj.example.recyclerview.widgets

import android.os.Bundle

import android.util.Log
import android.view.View
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.recyclerview.SlidingLayout
import com.lwj.wants.recyclerview.animation.AnimationSlidingLayout
import com.lwj.wants.util.CountDownTimer
import kotlinx.android.synthetic.main.activity_animation_sliding.*


class AnimationSlidingActivity : BaseActivity() {
    private val TAG = "Animation"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_sliding)
        setListener()
       // initAction()
    }

    private fun setListener(){
        anim_sliding.setSlidingResultListener(object :AnimationSlidingLayout.OnSlidingResultListener{
            override fun onRefresh() {
                  Log.i(TAG,"AnimationSlidingActivity_onRefresh: $  刷新")
                initAction()
            }
        })
    }


    private fun initAction() {
        CountDownTimer(4000, 1000)
            .setOnCountDownListener(object : CountDownTimer.OnCountDownListener {
                override fun onTick(remain: Long) {

                }

                override fun onFinish() {
                   anim_sliding.stop(SlidingLayout.SlidingLayout.VIEW_TOP)
                }
            }).start()

    }
}