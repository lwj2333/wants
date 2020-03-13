package com.lwj.example.imagview

import android.os.Bundle
import android.util.Log
import android.view.View
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.util.NumberUtils
import kotlinx.android.synthetic.main.activity_imagview.*

class ImageViewActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagview)
        bt_guaguaka.setOnClickListener(this)
        bt_loading.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_guaguaka -> {
                jumpActivity(GuaGuaKaActivity::class.java)
            }
            R.id.bt_loading -> {
           jumpActivity(LoadingActivity::class.java)
            }
        }
    }
}