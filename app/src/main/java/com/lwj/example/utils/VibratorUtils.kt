package com.lwj.example.utils

import android.app.Activity
import android.app.Service
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator


/**
 * @author by  LWJ
 * @date on 2018/8/30
 * @describe 添加描述
 */
class VibratorUtils {

    companion object {
        //震动milliseconds毫秒
        fun vibrate(activity: Activity, milliseconds: Long, amplitude: Int) {
            val vib: Vibrator = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (vib.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val vibrationEffect = VibrationEffect.createOneShot(milliseconds, amplitude)
                    vib.vibrate(vibrationEffect)
                } else {
                    vib.vibrate(milliseconds)
                }
            }
        }

        //以pattern[]方式震动
        fun vibrate(activity: Activity, pattern: LongArray, repeat: Int) {
            val vib: Vibrator = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (vib.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val vibrationEffect = VibrationEffect.createWaveform(pattern, repeat)
                    vib.vibrate(vibrationEffect)
                } else {
                    vib.vibrate(pattern, repeat)
                }


            }
        }
        //取消震动
        fun virateCancle(activity: Activity) {
            val vib: Vibrator = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (vib.hasVibrator()) {
                vib.cancel()
            }
        }
    }


    /**
     * 创建一次性振动
     *
     * @param milliseconds 震动时长（ms）
     * @param amplitude 振动强度。这必须是1到255之间的值，或者DEFAULT_AMPLITUDE
     */
    //  VibrationEffect vibrationEffect = VibrationEffect.createOneShot(long milliseconds, int amplitude);

    /**
     * 创建波形振动
     *
     * @param timings 交替开关定时的模式，从关闭开始。0的定时值将导致定时/振幅对被忽略。
     * @param repeat 振动重复的模式，如果您不想重复，则将索引放入计时数组中重复，或者-1。
     *               -1 为不重复
     *               0 为一直重复振动
     *               1 则是指从数组中下标为1的地方开始重复振动，重复振动之后结束
     *               2 从数组中下标为2的地方开始重复振动，重复振动之后结束
     */
    //  VibrationEffect vibrationEffect = VibrationEffect.createWaveform(long[] timings, int repeat);

    /**
     * 创建波形振动
     *
     * @param timings 振幅值中的定时值。定时值为0振幅可忽视的。
     * @param amplitudes 振幅值中的振幅值。振幅值必须为0和255之间，或为DEFAULT_AMPLITUDE。振幅值为0意味着断开。
     * @param repeat 振动重复的模式，如果您不想重复，则将索引放入计时数组中重复，或者-1。
     */
    //   VibrationEffect vibrationEffect = VibrationEffect.createWaveform(long[] timings, int[] amplitudes, int repeat);


}
