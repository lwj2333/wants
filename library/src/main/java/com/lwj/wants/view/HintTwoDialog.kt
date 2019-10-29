package com.lwj.wants.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.lwj.wants.R
import kotlinx.android.synthetic.main.dialog_two_hint.*

/**
 * @author by  LWJ
 * @date on 2019/10/29
 * @describe 添加描述
 */
class HintTwoDialog :Dialog, View.OnClickListener {
    constructor(context: Context) : super(context,R.style.DialogTheme)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutID)
    }


    override fun onStart() {
        super.onStart()
        val lp = window?.attributes
        window?.setGravity(Gravity.CENTER)
        val displayMetrics = context.resources.displayMetrics
        lp?.width = (displayMetrics.widthPixels * 0.7f).toInt()
        window?.attributes = lp
        initView()
    }

    private var content: String? = null
    private var cancel:String? = null
    private var ensure:String? = null
    private var layoutID = R.layout.dialog_two_hint
    fun setListener(listener: HintTwoDialogResultListener): HintTwoDialog {
        this.listener = listener
        return this
    }

    private var listener: HintTwoDialogResultListener? = null

    private fun initView() {
        tv_ensure.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
        if (!TextUtils.isEmpty(content)) {
            tv_content.text = content
        }

        if (!TextUtils.isEmpty(cancel)) {
            tv_cancel.text = cancel
        }
        if (!TextUtils.isEmpty(ensure)) {
            tv_ensure.text = ensure
        }
        tv_content.gravity = gravity
    }

    private var gravity = Gravity.NO_GRAVITY
    fun setTextGravity(gravity: Int): HintTwoDialog {
        this.gravity = gravity
        return this
    }

    fun setContent(content: String): HintTwoDialog {
        this.content = content
        return this
    }

    fun setCancel(cancel: String): HintTwoDialog {
        this.cancel = cancel
        return this
    }

    fun setLayout(id: Int): HintTwoDialog {
        this.layoutID = id
        return this
    }

    fun setEnsure(ensure: String): HintTwoDialog {
        this.ensure = ensure
        return this
    }

    fun cancelable(cancelable: Boolean): HintTwoDialog {
        this.setCancelable(cancelable)
        return this
    }

    fun canceledOnTouchOutside(cancelable: Boolean): HintTwoDialog {
        this.setCanceledOnTouchOutside(cancelable)
        return this
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tv_cancel) {
            if (listener != null) {
                listener!!.onListener(this, false)
            }
        } else if (id == R.id.tv_ensure) {
            if (listener != null) {
                listener!!.onListener(this, true)
            }
        }
    }

    interface HintTwoDialogResultListener {
        fun onListener(dialog: HintTwoDialog, b: Boolean)
    }
}