package com.lwj.example.imagview

import android.os.Bundle
import android.view.View
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.util.NumberUtils
import kotlinx.android.synthetic.main.activity_loading.*


class LoadingActivity :BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        button.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
    }

    private val TAG = "ImageViewActivity"
    private var progress: Double = 0.0
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {
                img_rotate.startViewAutoRotate(1000,true)
            }
            R.id.button2 -> {
                progress = NumberUtils.add(progress, 0.1).toDouble()
                if (progress > 1) {
                    progress = 0.1
                }
                img_rotate.setProgressRotate(progress.toFloat())
            }
            R.id.button3->{
                img_rotate.endAnimator()
            }
            R.id.button4->{
                img_rotate.cancel()
            }
            R.id.button5->{
                img_rotate.reverse()
            }
        }
    }
}