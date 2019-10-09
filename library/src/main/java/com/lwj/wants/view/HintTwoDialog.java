package com.lwj.wants.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lwj.wants.R;


/**
 * author by  LWJ
 * date on  2018/2/22.
 * describe 添加描述
 */
public class HintTwoDialog extends Dialog implements View.OnClickListener {
    public HintTwoDialog(@NonNull Context context) {
        super(context,R.style.DialogTheme);
    }

    public HintTwoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);

        initView();
    }

    private String content,cancel,ensure;
    private int layoutID = R.layout.dialog_two_hint ;
    public HintTwoDialog setListener(HintTwoDialogResultListener listener) {
        this.listener = listener;
        return this;
    }

    private HintTwoDialogResultListener listener;

    private void initView() {

        TextView tv_content = findViewById(R.id.tv_content);
        TextView tv_cancel = findViewById(R.id.tv_cancel);
        TextView tv_ensure = findViewById(R.id.tv_ensure);
        tv_ensure.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        if (!TextUtils.isEmpty(content)){
            tv_content.setText(content);
        }

        if (!TextUtils.isEmpty(cancel)){
            tv_cancel.setText(cancel);
        }
        if (!TextUtils.isEmpty(ensure)){
            tv_ensure.setText(ensure);
        }
        tv_content.setGravity(gravity);
    }
    private int gravity = Gravity.NO_GRAVITY;
    public HintTwoDialog setGravity(int gravity) {
       this.gravity =gravity;
        return this;
    }

    public HintTwoDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public HintTwoDialog setCancel(String cancel) {
        this.cancel = cancel;
        return this;
    }
    public HintTwoDialog setLayout(int id) {
        this.layoutID = id;
        return this;
    }
    public HintTwoDialog setEnsure(String ensure) {
        this.ensure = ensure;
        return this;
    }
    public HintTwoDialog cancelable(boolean cancelable){
        this.setCancelable(cancelable);
        return this;
    }
    public HintTwoDialog canceledOnTouchOutside(boolean cancelable){
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            if (listener != null) {
                listener.onListener(this, false);
            }
        } else if (id == R.id.tv_ensure) {
            if (listener != null) {
                listener.onListener(this, true);
            }
        }
    }
    public interface HintTwoDialogResultListener{
        void onListener(HintTwoDialog dialog, boolean b);
    }
}
