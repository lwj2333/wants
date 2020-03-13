package com.lwj.example.imagview

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.lwj.example.BaseActivity
import com.lwj.example.R
import kotlinx.android.synthetic.main.activity_guagua.*

class GuaGuaKaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guagua)
        bt_rest.setOnClickListener {
          gg.rest(ContextCompat.getDrawable(this,R.drawable.mountain))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gg.recycle()
    }
}