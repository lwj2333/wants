package com.lwj.wants.recyclerview.animation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.lwj.wants.R
import java.util.*


class AnimationTop :FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
            initView()
    }

    private var tv_state :TextView? = null
    private var tv_date :TextView? = null
    private var img_state:ImageView?=null
    private var progressBar: ProgressBar?=null

    private fun initView(){
        val view =    LayoutInflater.from(context).inflate(R.layout.item_animation_top,this,true)

        img_state = view.findViewById(R.id.img_state)
        progressBar = view.findViewById(R.id.progressBar)
        tv_date = view.findViewById(R.id.tv_date)
        tv_state = view.findViewById(R.id.tv_state)
        getDate()

    }

    fun hint() {
        tv_state?.setText(R.string.pull_down)
        img_state?.visibility = View.VISIBLE
        img_state?.setBackgroundResource(R.drawable.ic_action_downward)
        if (!TextUtils.isEmpty(date)) {
            tv_date?.visibility = View.VISIBLE
            tv_date?.text = date
        }
    }

    fun action() {
        tv_state?.setText(R.string.release)
        img_state?.setBackgroundResource(R.drawable.ic_action_upward)
    }

    fun run() {
        tv_state?.setText(R.string.refresh)
        img_state?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    fun finish() {
        tv_state?.setText(R.string.succeed)
        progressBar?.visibility = View.GONE
    }

    private var date = ""
    //private static final String TAG = "TopView";
    private fun getDate() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //  Log.i(TAG, "getDate: "+minute);
        val hourStr: String
        val minuteStr: String
        if (hour < 10) {
            hourStr = "0$hour"
        } else {
            hourStr = hour.toString()
        }
        if (minute < 10) {
            minuteStr = ":0$minute"
        } else {
            minuteStr = ":$minute"
        }
        date = resources.getString(R.string.update) + " " + hourStr + minuteStr
    }
}