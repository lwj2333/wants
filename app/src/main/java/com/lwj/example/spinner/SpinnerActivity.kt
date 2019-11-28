package com.lwj.example.spinner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.lwj.example.BaseActivity
import com.lwj.example.R
import kotlinx.android.synthetic.main.activity_spinner.*


class SpinnerActivity :BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        val array = resources.getStringArray(R.array.week)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.i(TAG,"SpinnerActivity: $ 没有被选中 ")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i(TAG,"SpinnerActivity: 选中${array[position]}  ")
            }
        }
        CSpinner.addData(array.toList())
        CSpinner.setOnCSpinnerItemListener {

        }


    }
    private val TAG ="SpinnerActivity"
}