package com.lwj.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.lwj.wants.view.HintTwoDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt1.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt1->{
                HintTwoDialog(this).setCancel("否").setEnsure("是").setGravity(Gravity.CENTER)
                    .setListener { dialog, b ->
                        if (b) {
                         showToast("删除")
                    }
                        dialog.dismiss()
                    }.setContent("是否删除？").show()
            }
        }

    }
}
