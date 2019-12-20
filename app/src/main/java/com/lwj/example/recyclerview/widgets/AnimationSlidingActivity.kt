package com.lwj.example.recyclerview.widgets

import android.os.Bundle
import android.util.Log

import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.recyclerview.SlidingLayout
import com.lwj.wants.recyclerview.example.SimpleSlidingLayout
import com.lwj.wants.util.CountDownTimer
import kotlinx.android.synthetic.main.activity_animation_sliding.*


class AnimationSlidingActivity : BaseActivity() {
    private val TAG = "Animation"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_sliding)
        setListener()
    }

    private fun setListener(){
        anim_sliding.setSlidingResultListener(object :SimpleSlidingLayout.OnSlidingResultListener{
            override fun onLoad() {
                initBottom()
            }

            override fun onRefresh() {
                  Log.i(TAG,"AnimationSlidingActivity_onRefresh: $ ahahhahah ")
                initTop()
            }
        })
    }
    private fun initBottom() {
        CountDownTimer(5000, 1000)
            .setOnCountDownListener(object : CountDownTimer.OnCountDownListener {
                override fun onTick(remain: Long) {

                }

                override fun onFinish() {
                    anim_sliding.stop(SlidingLayout.SlidingLayout.VIEW_BOTTOM)
                }
            }).start()

    }

    private fun initTop() {
        CountDownTimer(10000, 1000)
            .setOnCountDownListener(object : CountDownTimer.OnCountDownListener {
                override fun onTick(remain: Long) {

                }

                override fun onFinish() {
                   anim_sliding.stop(SlidingLayout.SlidingLayout.VIEW_TOP)
                }
            }).start()

    }
}