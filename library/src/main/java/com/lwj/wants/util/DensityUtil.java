package com.lwj.wants.util;

import android.content.Context;
      
    public class DensityUtil {


        /**
         *  dp 转像素
         * @param context 上下文
         * @param dpValue  dp
         * @return  计算后返回的像素
         */
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;  
            return (int) (dpValue * scale + 0.5f);  
        }


        /** 像素转dp
         * @param context 上下文
         * @param pxValue 像素
         * @return  计算后返回的dp
         */
        public static int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;  
            return (int) (pxValue / scale + 0.5f);  
        }  
    }  