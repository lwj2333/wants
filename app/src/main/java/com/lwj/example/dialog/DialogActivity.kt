package com.lwj.example.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.util.ProgressDialogUtil
import com.lwj.wants.widget.dialog.HintTwoDialog
import kotlinx.android.synthetic.main.activity_dialog.*
import skin.support.SkinCompatManager

class DialogActivity :BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        bt1.setOnClickListener(this)
        bt2.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt1->{
                HintTwoDialog(this).setCancel("否").setEnsure("是").setTextGravity(Gravity.CENTER)
                    .setListener(object : HintTwoDialog.HintTwoDialogResultListener{
                        override fun onListener(dialog: HintTwoDialog, b: Boolean) {
                            if (b) {
                                showToast("删除")
                                SkinCompatManager.getInstance().loadSkin("night",
                                    null, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
                            }else{
                                SkinCompatManager.getInstance().restoreDefaultTheme()
                            }
                            dialog.dismiss()
                        }
                    })
                    .setContent("是否删除？")
                    .show()

            }
            R.id.bt2->{
//                ContextCompat.getDrawable(this,R.color.baby_blue)?.let {
//                    ProgressDialog(this).setContent("正在加载中").setDrawable(
//                        it
//                    ).show()
//                }
                // ProgressDialog(this).setMessage("正在加载中").show()
                ProgressDialogUtil.showProgressDialog(this)
            }
        }

    }
}