package com.lwj.wants.spinner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.lwj.wants.R;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * author by  LWJ
 * date on  2018/2/7.
 * describe 添加描述
 */
public class CSpinner extends LinearLayout {
    private Context context;
    private int spinner_icon;
    private TextView tv;
    private ImageView img;
    private RecyclerView recyclerView;
    private PopupWindow pw;

    private RecyclerView.Adapter<ViewHolder> adapter;
    private List<String> list = new ArrayList<>();
    private OnCSpinnerItemListener listener;


    private float spinner_text_size;
    private int spinner_text_color;
    private String spinner_default_content;
    private int durationTime;
    private int spinner_popup_item_drawable;

    public CSpinner(Context context) {
        super(context);
    }

    public CSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CSpinner);
        spinner_text_size = typedArray.getDimensionPixelSize(R.styleable.CSpinner_spinner_text_size, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, getResources().getDisplayMetrics()));
        spinner_text_color = typedArray.getColor(R.styleable.CSpinner_spinner_text_color, getResources().getColor(R.color.black));
        spinner_icon = typedArray.getInteger(R.styleable.CSpinner_spinner_icon, R.drawable.ic_action_spinner_drop);
        spinner_default_content = typedArray.getString(R.styleable.CSpinner_spinner_default_content);
        durationTime = typedArray.getInteger(R.styleable.CSpinner_durationTime, 300);
        spinner_popup_item_drawable = typedArray.getInteger(R.styleable.CSpinner_spinner_popup_item_drawable, R.drawable.bg_ripple);
        typedArray.recycle();
        init();
        this.measureDisplayHeight();
    }


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_spinner, this, true);

        tv = view.findViewById(R.id.cspinner_tv_spinner);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, spinner_text_size);
        tv.setTextColor(spinner_text_color);

        if (!TextUtils.isEmpty(spinner_default_content)) {
            tv.setText(spinner_default_content);
        }

        img = view.findViewById(R.id.cspinner_img_spinner);
        img.setImageDrawable(getResources().getDrawable(spinner_icon));

        //Todo 异常
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick(500)) {
                    return;
                }
                if (list.size() > 0) {
                    if (!isArrowHidden) {
                        showDropDown();
                    }
                }

            }
        });


    //     this.setBackground(getResources().getDrawable(R.drawable.bg_ripple));


        recyclerView = new RecyclerView(context);
        // View v =inflater.inflate(R.layout.item_popup_list,this,false);
        // recyclerView = v.findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        pw = new PopupWindow(context);

        pw.setContentView(recyclerView);
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);

        adapter = new RecyclerView.Adapter<ViewHolder>() {

            @NotNull
            @Override
            public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_popup_list_textview, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
                holder.tv.setText(list.get(position));
                holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, spinner_text_size);
                holder.tv.setGravity(Gravity.CENTER_VERTICAL);
            //    holder.tv.setBackgroundResource(spinner_popup_item_drawable);
                holder.tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setText(holder.getAdapterPosition());
                        dismissDropDown();
                        if (listener != null) {
                            listener.onCSpinnerItemListener(index);
                        }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        };
        recyclerView.setAdapter(adapter);
        this.pw.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.spinner_drawable));
//        if(Build.VERSION.SDK_INT >= 21) {
//            this.pw.setElevation(16.0F);
//
//        } else {
//            this.pw.setBackgroundDrawable(ContextCompat.getDrawable(context, drawable.drop_down_shadow));
//        }

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if (isArrowHidden) {
                    hideAnimation();
                }
            }
        });
    }


    private int displayHeight;
    private int parentVerticalOffset;

    private int getParentVerticalOffset() {
        if (this.parentVerticalOffset > 0) {
            return this.parentVerticalOffset;
        } else {
            int[] locationOnScreen = new int[2];
            this.getLocationOnScreen(locationOnScreen);
            return this.parentVerticalOffset = locationOnScreen[1];
        }
    }

    private void measureDisplayHeight() {
        this.displayHeight = this.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    private void measurePopUpDimension() {
        int widthSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(this.displayHeight - this.getParentVerticalOffset() - this.getMeasuredHeight(), MeasureSpec.UNSPECIFIED);
        recyclerView.measure(widthSpec, heightSpec);
        pw.setWidth(recyclerView.getMeasuredWidth());
        pw.setHeight(recyclerView.getMeasuredHeight());
    }

    public void dismissDropDown() {
        hideAnimation();
        pw.dismiss();
    }

    public void showDropDown() {
        showAnimation();
        measurePopUpDimension();
        pw.showAsDropDown(this);
    }

    public void setOnCSpinnerItemListener(OnCSpinnerItemListener listener) {
        this.listener = listener;
    }

    private int index = -1;
    private String str;

    private void setText(int position) {

        if (list == null || list.size() < 1) {
            return;
        }
        if (index == -1) {
            str = list.get(position);
            tv.setText(str);
            index = position;
            list.remove(position);
            return;
        }
        String temp = list.get(position);
        tv.setText(temp);
        list.add(index, str);
        str = temp;
        if (position >= index) {
            position = position + 1;
        }
        list.remove(position);
        index = position;

        adapter.notifyDataSetChanged();
    }


    public void addData(List<String> list) {
        addData(list,0);
    }

    public void addData(List<String> list, int position) {
        if (list != null) {
            index = -1;
            str = null;
            this.list = list;
            if (position < 0) {
                setText(0);
            } else {
                setText(position);
            }

        }
    }


    public int getIndex() {
        return this.index;
    }

    public void restList() {
        list.add(index, str);
        index = -1;
        str = null;
    }

    public boolean isArrowHidden() {
        return isArrowHidden;
    }

    private boolean isArrowHidden;
    private ObjectAnimator anim;
    private void showAnimation() {
         anim = ObjectAnimator.ofFloat(img, View.ROTATION, 0, 180);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        anim.setDuration(durationTime);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isArrowHidden = true;
            }
        });

        anim.start();
    }

    private void hideAnimation() {
         anim = ObjectAnimator.ofFloat(img, View.ROTATION, 180, 0);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        anim.setDuration(durationTime);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isArrowHidden = false;
            }
        });

        anim.start();
    }



    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    private long lastClickTime;

    /**
     * 防止重复点击
     */
    protected boolean isFastDoubleClick(int limitTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD > 0 && timeD < limitTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


//    public boolean onTouchEvent(MotionEvent event) {
//        Log.i(TAG, "onTouchEvent: "+event.getAction());
//        if(this.isEnabled() && event.getAction() == 1) {
//            if(!this.pw.isShowing()) {
//                this.showDropDown();
//            } else {
//                this.dismissDropDown();
//            }
//        }
//        return super.onTouchEvent(event);
//    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.cspinner_tv_content);
        }

    }

    public interface OnCSpinnerItemListener {
        void onCSpinnerItemListener(int position);
    }
}
