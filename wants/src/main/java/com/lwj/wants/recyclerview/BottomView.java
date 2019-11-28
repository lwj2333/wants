package com.lwj.wants.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * author by  LWJ
 * date on  2017/12/14.
 * describe 添加描述
 */
public class BottomView extends OutsideView {
    private TextView tv;
    public BottomView(@NonNull Context context) {
        super(context);
    }

    public BottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        tv = (TextView) getChildAt(0);
    }
    @Override
    public void hint() {
        tv.setText("下拉刷新");
    }

    @Override
    public void action() {
        tv.setText("释放立即刷新");
    }

    @Override
    public void run() {
        tv.setText("正在刷新");
    }

    @Override
    public void finish() {
        tv.setText("刷新成功");
    }


}
