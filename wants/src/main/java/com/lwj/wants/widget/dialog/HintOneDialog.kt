package com.lwj.wants.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.lwj.wants.R
import kotlinx.android.synthetic.main.dialog_one_hint.*


/**
 * @author by  LWJ
 * @date on 2019/10/29
 * @describe 添加描述
 */
class HintOneDialog : Dialog, View.OnClickListener {
    constructor(context: Context) : super(context, R.style.HintDialogTheme)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutID)
        val lp = window?.attributes
        window?.setGravity(Gravity.CENTER)
        val displayMetrics = context.resources.displayMetrics
        lp?.width = (displayMetrics.widthPixels * 0.7f).toInt()
        window?.attributes = lp
        initView()

    }
   private var title:String?=null
    private var content: String? = null
    private var ensure: String? = null
    private var layoutID = R.layout.dialog_one_hint
    fun setListener(listener: HintOneDialogResultListener): HintOneDialog {
        this.listener = listener
        return this
    }

    private var listener: HintOneDialogResultListener? = null

    private fun initView() {
        tv_dialog_one_hint_ensure.setOnClickListener(this)
        if (!TextUtils.isEmpty(title)) {
            tv_dialog_one_hint_title.text = title
        }
        if (!TextUtils.isEmpty(content)) {
            tv_dialog_one_hint_content.text = content
        }
        if (!TextUtils.isEmpty(ensure)) {
            tv_dialog_one_hint_ensure.text = ensure
        }
        tv_dialog_one_hint_content.gravity = gravity
    }

    private var gravity = Gravity.NO_GRAVITY
    fun setTextGravity(gravity: Int): HintOneDialog {
        this.gravity = gravity
        return this
    }

    fun setContent(content: String): HintOneDialog {
        this.content = content
        return this
    }
    fun setTitle(title: String): HintOneDialog {
        this.title = title
        return this
    }

    fun setLayout(id: Int): HintOneDialog {
        this.layoutID = id
        return this
    }

    fun setEnsure(ensure: String): HintOneDialog {
        this.ensure = ensure
        return this
    }

    fun cancelable(cancelable: Boolean): HintOneDialog {
        this.setCancelable(cancelable)
        return this
    }

    fun canceledOnTouchOutside(cancelable: Boolean): HintOneDialog {
        this.setCanceledOnTouchOutside(cancelable)
        return this
    }

    override fun onClick(v: View) {
        if (listener != null) {
            listener!!.onListener(this)
        }
    }

    interface HintOneDialogResultListener {
        fun onListener(dialog: HintOneDialog)
    }
}