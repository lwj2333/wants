package com.lwj.wants.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import com.lwj.wants.R

import kotlinx.android.synthetic.main.dialog_progress_item.*

class ProgressDialog :Dialog {
    constructor(context: Context) : super(context, R.style.DialogTheme)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        tv_dialog_progress.text=msg
    }
  private var msg :String?=null
    fun setContent(msg:String):ProgressDialog{
        this.msg =msg
        return this
    }

}