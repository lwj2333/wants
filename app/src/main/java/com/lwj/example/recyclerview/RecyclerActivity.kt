package com.lwj.example.recyclerview

import android.os.Bundle
import android.view.View
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.example.recyclerview.widgets.AnimationSlidingActivity
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_simple -> {
               jumpActivity(SimpleActivity::class.java)
            }
            R.id.bt_more -> {
                jumpActivity(MoreActivity::class.java)
            }
            R.id.bt_sliding->{
                jumpActivity(SlidingActivity::class.java)
            }
            R.id.bt_anim->{
                jumpActivity(AnimationSlidingActivity::class.java)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        bt_more.setOnClickListener(this)
        bt_simple.setOnClickListener(this)
        bt_sliding.setOnClickListener(this)
        bt_anim.setOnClickListener(this)
    }


}