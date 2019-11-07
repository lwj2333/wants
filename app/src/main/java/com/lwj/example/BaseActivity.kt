package com.lwj.example

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }
    protected fun jumpActivity(activityClass: Class<*>) {
        val it = Intent(this, activityClass)
        startActivity(it)
    }
    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
    }
}