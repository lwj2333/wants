package com.lwj.example

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }
    protected fun jumpActivity(activityClass: Class<*>) {
        val it = Intent(this, activityClass)
        startActivity(it)
    }
}