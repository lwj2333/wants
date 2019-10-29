package com.lwj.wants.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.lwj.wants.R
import kotlinx.android.synthetic.main.dialog_progress.*


/**
 * author by  LWJ
 * date on  2019/10/29.
 * describe 添加描述
 */
class ProgressDialog :Dialog {
    constructor(context: Context) : super(context, R.style.DialogTheme)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)

    }

    private var msg :String?=null
    fun setContent(msg:String):ProgressDialog{
        this.msg =msg
        return this
    }
    fun cancelable(cancelable: Boolean): ProgressDialog {
        this.setCancelable(cancelable)
        return this
    }
    fun canceledOnTouchOutside(cancelable: Boolean): ProgressDialog {
        this.setCanceledOnTouchOutside(cancelable)
        return this
    }
    override fun onStart() {
        super.onStart()
        val lp = window?.attributes
        window?.setGravity(Gravity.CENTER)
        val displayMetrics = context.resources.displayMetrics
        lp?.width = (displayMetrics.widthPixels * 0.9f).toInt()
        window?.attributes = lp
        tv_dialog_progress.text=msg
    }
}