package com.lwj.wants.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * author BuyProductActivity  LWJ
 * date on  2018/2/9.
 * describe 添加描述
 */
public class DateUtils {
    public static String formatDateTime(long date ) {

        return formatDateTime(date,null);
    }
    public static String formatDateTime(long date,String template ) {
        if (date == 0) {
            return null;
        }
        if (TextUtils.isEmpty(template)) {
            template = "yyyy-MM-dd    HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(template,
                Locale.getDefault());
        return format.format(date);
    }

    public static String formatAudioTime(int time) {
        if (time < 60) {
            return String.format("00:%02d", time % 60);
        } else if (time < 3600) {
            return String.format("%02d:%02d", time / 60, time % 60);
        } else {
            return String.format("%02d:%02d:%02d", time / 3600, time % 3600 / 60, time % 60);
        }
    }

    public static long getCurrentDateLong() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static int[] getCurrentDateArray() {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);
        int[] array = new int[6];
        array[0] = year;
        array[1] = month;
        array[2] = day;
        array[3] = hour;
        array[4] = minute;
        array[5] = second;
        return array;
    }

    public static long dateToTimestamp(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

}
