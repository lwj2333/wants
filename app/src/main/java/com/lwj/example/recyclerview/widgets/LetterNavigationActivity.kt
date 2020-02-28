package com.lwj.example.recyclerview.widgets

import android.os.Bundle
import com.lwj.example.BaseActivity
import com.lwj.example.R
import kotlinx.android.synthetic.main.activity_letter_navigation.*

class LetterNavigationActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_letter_navigation)
        lv.setOnClickListener {

        }

    }
}