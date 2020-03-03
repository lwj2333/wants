package com.lwj.example.recyclerview.widgets

import android.os.Bundle
import android.util.Log
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.recyclerview.widgets.LetterNavigationView
import kotlinx.android.synthetic.main.activity_letter_navigation.*

class LetterNavigationActivity :BaseActivity(){
    private val TAG ="LetterNavigation"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_letter_navigation)


     lv.setNavigationChangeListener(object :LetterNavigationView.OnNavigationChangeListener{
         override fun onChange(letter: String, position: Int) {
               Log.i(TAG,"LetterNavigationActivity_onChange: $letter $position  ")
         }
     })
    }
}