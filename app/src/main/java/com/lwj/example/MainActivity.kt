package com.lwj.example




import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.lwj.example.dialog.DialogActivity
import com.lwj.example.edittext.EditTextActivity
import com.lwj.example.imagview.ImageViewActivity
import com.lwj.example.recyclerview.RecyclerActivity
import com.lwj.example.spinner.SpinnerActivity
import com.lwj.example.tab.TabActivity
import com.lwj.example.textview.TextViewActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_dialog.setOnClickListener(this)
        bt_tab.setOnClickListener(this)
        bt_recycler.setOnClickListener(this)
        bt_spinner.setOnClickListener(this)
        bt_text.setOnClickListener(this)
        bt_edittext.setOnClickListener(this)
        bt_imageview.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_dialog->{
                jumpActivity(DialogActivity::class.java)
            }
            R.id.bt_tab->{
                jumpActivity(TabActivity::class.java)
            }
            R.id.bt_recycler->{
                jumpActivity(RecyclerActivity::class.java)
            }
            R.id.bt_spinner->{
                jumpActivity(SpinnerActivity::class.java)
            }
            R.id.bt_text->{
                jumpActivity(TextViewActivity::class.java)
            }
            R.id.bt_edittext->{
                jumpActivity(EditTextActivity::class.java)
            }
            R.id.bt_imageview->{
                jumpActivity(ImageViewActivity::class.java)
            }
        }

    }
}
