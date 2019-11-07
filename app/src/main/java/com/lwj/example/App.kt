package com.lwj.example

import android.app.Application
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //皮肤控件初始化
        SkinCompatManager.withoutActivity(this)
            .addInflater( SkinAppCompatViewInflater())
            .setSkinStatusBarColorEnable(true)
            .loadSkin()
    }
}