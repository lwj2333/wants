package com.lwj.wants.recyclerview.example

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.lwj.wants.R
import com.lwj.wants.util.DateUtils



class SimpleTopView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private var tvState: TextView? = null
    private var tvDate: TextView? = null
    private var imgState: ImageView? = null
    private var progressBar: ProgressBar? = null

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.item_animation_top, this, true)

        imgState = view.findViewById(R.id.img_state)
        progressBar = view.findViewById(R.id.progressBar)
        tvDate = view.findViewById(R.id.tv_date)
        tvState = view.findViewById(R.id.tv_state)

    }

    fun hint() {
        tvState?.setText(R.string.pull_down)
        imgState?.visibility = View.VISIBLE
        imgState?.setBackgroundResource(R.drawable.ic_action_downward)
        if (!TextUtils.isEmpty(date)) {
            tvDate?.visibility = View.VISIBLE
            tvDate?.text = date
        }
    }


    fun action() {
        tvState?.setText(R.string.release)
        imgState?.setBackgroundResource(R.drawable.ic_action_upward)
    }

    fun run() {
        tvState?.setText(R.string.refresh)
        imgState?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    fun finish() {
        tvState?.setText(R.string.succeed)
        progressBar?.visibility = View.GONE
        getDate()
    }

    private var date: String? = null
    private fun getDate() {
        date = resources.getString(R.string.update) + " " + DateUtils.formatDateTime(
            DateUtils.getCurrentDateLong(),
            "HH:mm:ss"
        )
    }
}