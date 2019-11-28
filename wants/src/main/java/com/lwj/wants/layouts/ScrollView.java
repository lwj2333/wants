package com.lwj.wants.layouts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lwj.wants.recyclerview.util.Instrument;

/**
 * author by  LWJ
 * date on  2017/12/25.
 * describe 添加描述
 */
public class ScrollView extends FrameLayout {

    private ViewGroup scrollLayout;

    public ScrollView(@NonNull Context context) {
        super(context);
    }

    public ScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        scrollLayout = (ViewGroup)getChildAt(0);


         measureChild(scrollLayout,widthMeasureSpec,heightMeasureSpec);



        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout: "+scrollLayout.getMeasuredWidth()+" "+scrollLayout.getMeasuredHeight());

        super.onLayout(changed, left, top, right, bottom);

    }
    private OnTouchListener mDelegateTouchListener;
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mDelegateTouchListener = l;
    }

    public void setmSlidingListener(SlidingListener mSlidingListener) {
        this.mSlidingListener = mSlidingListener;
    }

    private SlidingListener mSlidingListener;
    public static final int SLIDING_DISTANCE_UNDEFINED = -1;
    private int mSlidingPointerMode = 0;
    public static final int SLIDING_POINTER_MODE_ONE = 0;
    public static final int SLIDING_POINTER_MODE_MORE = 1;
    private int mSlidingMode = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDelegateTouchListener != null && mDelegateTouchListener.onTouch(this, event)) {
            return true;
        }
        final  int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                float delta = 0.0f;
                float move = 0.0f;
//                int activePointerId = event.getPointerId(event.getPointerCount() - 1);
//                if (mActivePointerId != activePointerId) {
//                    mActivePointerId = activePointerId;
//                    mInitialDownY = getMotionEventY(event, mActivePointerId);
//                    mInitialMotionY = mInitialDownY + mTouchSlop;
//                    if (mSlidingListener != null) {
//                        mSlidingListener.onSlidingChangePointer(scrollLayout, activePointerId);
//                    }
//                }
                delta = event.getY() - mLastMotionY;
                delta = getInstrument().getTransLationY(scrollLayout) + delta ;
                mLastMotionY =event.getY();
                   getInstrument().slidingByDeltaToY(getScrollLayout(),delta);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:


                break;
        }

        return true;
    }

    private static final String TAG = "ScrollView";
    private boolean mIsBeingDragged=false;

    public ViewGroup getScrollLayout() {
        return scrollLayout;
    }

    public float getSlidingDistance() {
        return getInstrument().getTransLationY(getScrollLayout());
    }
    private int mActivePointerId = INVALID_POINTER;
    private static final int INVALID_POINTER = -1;
    private float mInitialDownY; //初始 按下的 Y位置
    private int mTouchSlop;//系统允许最小的滑动判断值
    private float mInitialMotionY;//滑动的距离
    private float mLastMotionY;//最后 按下的 Y位置
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);

                if (initialDownY == -1) {
                    return false;
                }
                mInitialDownY = initialDownY;

                break;
            case MotionEvent.ACTION_MOVE:

                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);

                if (y == -1) {
                    return false;
                }

                if (y > mInitialDownY) {
                    //判断是否是下滑刷新
                    final float yDiff = y - mInitialDownY;
                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollUp()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop;
                        mLastMotionY = mInitialMotionY;
                        mIsBeingDragged = true;
                    }
                } else if (y < mInitialDownY) {
                    //判断是否是上拉加载
                    final float yDiff = mInitialDownY - y;

                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollDown()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop;
                        mLastMotionY = mInitialMotionY;
                        mIsBeingDragged = true;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return mIsBeingDragged;
    }
    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1;
        }
        return ev.getY(index);
    }
    public Instrument getInstrument() {
        return Instrument.getInstance();
    }
    /**
     * @return 判断子控件是否可以向下滑动   -1 下滑
     */
    private boolean canChildScrollUp() {

        return scrollLayout.canScrollVertically(-1);
    }

    /**
     * @return 判断子控件是否可以向上滑动   1 上拉
     */
    private boolean canChildScrollDown() {
        return scrollLayout.canScrollVertically(1);
    }
    public interface SlidingListener {
        void onSlidingOffset(View v, float delta);

        void onSlidingStateChange(View v, int state);

        void onSlidingChangePointer(View v, int pointerId);
    }
}
