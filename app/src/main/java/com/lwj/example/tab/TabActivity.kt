package com.lwj.example.tab

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.widget.dialog.HintTwoDialog
import com.lwj.wants.widget.tab.model.RadioItem
import kotlinx.android.synthetic.main.activity_tab.*
import skin.support.SkinCompatManager
import java.util.ArrayList

class TabActivity :BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)
        initList()
        radio.orientation =LinearLayout.HORIZONTAL
        radio.setDrawableSize(60f)
        radio.addData(data)
        radio.check(0)
        tb.setOnClickListener {
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

    }
    private val data : ArrayList<RadioItem> = ArrayList()
    private fun initList(){

        data.add(
            RadioItem(
                "首页", R.drawable.bg_selector_tab_radiobutton,
                R.mipmap.ic_launcher, R.color.tc_selector_tab_radiobutton, true
            )
        )
        data.add(
            RadioItem(
                "购物车", R.drawable.bg_selector_tab_radiobutton,
                R.mipmap.ic_launcher, R.color.tc_selector_tab_radiobutton, false
            )
        )
        data.add(
            RadioItem(
                "我的", R.drawable.bg_selector_tab_radiobutton,
                R.mipmap.ic_launcher, R.color.tc_selector_tab_radiobutton, false
            )
        )
    }
}