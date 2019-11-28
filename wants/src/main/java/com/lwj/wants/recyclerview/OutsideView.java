package com.lwj.wants.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * author by  LWJ
 * date on  2017/12/15.
 * describe 添加描述
 */
public abstract class OutsideView extends FrameLayout {
    public OutsideView(@NonNull Context context) {
        super(context);
    }

    public OutsideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public abstract void hint();


    public abstract void action();


    public abstract  void run();


    public abstract void finish();




}
