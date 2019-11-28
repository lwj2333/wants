package com.lwj.wants.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwj.wants.R;

import java.util.Calendar;

/**
 * author by  LWJ
 * date on  2017/12/13.
 * describe 添加描述
 */
public  class TopView extends OutsideView {
     private TextView tv_state,tv_date;
     private ImageView img_state;
     private ProgressBar progressBar;

    public TopView(@NonNull Context context) {
        super(context);
    }

    public TopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void hint() {
        tv_state.setText(R.string.pull_down);
        img_state.setVisibility(VISIBLE);
        img_state.setBackgroundResource(R.drawable.ic_action_downward);
      if (!TextUtils.isEmpty(date)){
          tv_date.setVisibility(VISIBLE);
          tv_date.setText(date);
      }
    }

    @Override
    public void action() {
        tv_state.setText(R.string.release);
        img_state.setBackgroundResource(R.drawable.ic_action_upward);
    }

    @Override
    public void run() {
        tv_state.setText(R.string.refresh);
        img_state.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void finish() {
        tv_state.setText(R.string.succeed);
        progressBar.setVisibility(GONE);

    }


    private void init(){
        RelativeLayout relativeLayout = (RelativeLayout) getChildAt(0);
        img_state = relativeLayout.findViewById(R.id.img_state);
        progressBar = relativeLayout.findViewById(R.id.progressBar);
        LinearLayout layout = relativeLayout.findViewById(R.id.ll_text);
        tv_date = layout.findViewById(R.id.tv_date);
        tv_state= layout.findViewById(R.id.tv_state);
        getDate();
    }
    private String date = "c";
   //private static final String TAG = "TopView";
    private void getDate(){
        Calendar c = Calendar.getInstance();
        int hour =   c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
     //  Log.i(TAG, "getDate: "+minute);
        String hourStr ;
        String minuteStr ;
        if (hour<10){
            hourStr = "0"+hour;
        }else {
            hourStr = String.valueOf(hour);
        }
        if (minute<10){
            minuteStr = ":0"+minute;
        }else {
            minuteStr = ":"+minute;
        }
            date = getResources().getString(R.string.update)+" "+hourStr+minuteStr;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        init();
    }
}
