package com.lwj.example




import android.os.Bundle
import android.view.View
import com.lwj.example.dialog.DialogActivity
import com.lwj.example.recyclerview.RecyclerActivity
import com.lwj.example.tab.TabActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_dialog.setOnClickListener(this)
        bt_tab.setOnClickListener(this)
        bt_recycler.setOnClickListener(this)

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
        }

    }
}
