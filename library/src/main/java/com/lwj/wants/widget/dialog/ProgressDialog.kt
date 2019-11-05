package com.lwj.wants.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
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
    constructor(context: Context) : super(context, R.style.ProgressDialogTheme)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        val lp = window?.attributes
        window?.setGravity(Gravity.CENTER)
        val displayMetrics = context.resources.displayMetrics
        lp?.width = (displayMetrics.widthPixels * 0.9f).toInt()
        window?.attributes = lp
        initView()

    }

    private var msg :String?=null
    private var drawable: Drawable?=null

    fun setDrawable(drawable: Drawable): ProgressDialog {
        this.drawable=drawable
        return this
    }
    fun setMessage(msg:String): ProgressDialog {
        this.msg =msg
        if (tv_dialog_progress!=null){
            tv_dialog_progress.text=msg
        }
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

    private fun initView(){
        tv_dialog_progress.text=msg
        if (drawable!=null){
            pb_dialog_progress.indeterminateDrawable =drawable
        }
    }
}