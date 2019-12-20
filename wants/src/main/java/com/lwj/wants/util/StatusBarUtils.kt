package com.lwj.wants.util

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.Window
import android.view.WindowManager


/**
 * @author by  LWJ
 * @date on 2018/8/29
 * @describe 添加描述
 * @org  http://www.gdjiuji.com(广东九极生物科技有限公司)
 */
class StatusBarUtils {
    companion object {
       fun setWindowFullscreen(activity: Activity){
           val window = activity.window
         activity.requestWindowFeature(Window.FEATURE_NO_TITLE)// 隐藏标题
           window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
           WindowManager.LayoutParams.FLAG_FULLSCREEN)// 设置全屏
       }
        fun setWindowStatusBarColor(activity: Activity, colorResId: Int) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window = activity.window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = ContextCompat.getColor(activity, colorResId)

                    //底部导航栏
                    //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun setWindowStatusBarColor(dialog: Dialog, colorResId: Int) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window = dialog.window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = ContextCompat.getColor(dialog.context, colorResId)

                    //底部导航栏
                    //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun windowClearFullScreen(activity: Activity){
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}