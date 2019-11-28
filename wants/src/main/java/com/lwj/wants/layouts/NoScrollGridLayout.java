package com.lwj.wants.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lwj.wants.R;

/**
 * author by  LWJ
 * date on  2017/12/27.
 * describe 添加描述
 */
public class NoScrollGridLayout extends LinearLayout {
    private int count;
    private int rowMargin;
    private int columnMargin;


    public NoScrollGridLayout(Context context) {
        super(context);

    }

    public NoScrollGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoScrollGridLayout);
        count = typedArray.getInteger(R.styleable.NoScrollGridLayout_nsg_count,1);
        rowMargin = typedArray.getDimensionPixelSize(R.styleable.NoScrollGridLayout_nsg_rowMargin, 0);
        columnMargin = typedArray.getDimensionPixelSize(R.styleable.NoScrollGridLayout_nsg_columnMargin, 0);
        typedArray.recycle();
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRowMargin(int rowMargin) {
        this.rowMargin = rowMargin;
    }

    public void setColumnMargin(int columnMargin) {
        this.columnMargin = columnMargin;
    }

    private int width;
    private int vCount;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int vWidth;
        int vHeight = 0;

        vCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childHeight = 0;
        int childWidth = 0;
        if (vCount > 0) {
            View child = getChildAt(0);
            childHeight = child.getMeasuredHeight() + 2 * rowMargin;
            childWidth = child.getMeasuredWidth() + 2 * columnMargin;
        }

        for (int i = 0; i < vCount; i++) {
            if ((i + 1) % count == 0) {
                vHeight = vHeight + childHeight;
            }
            if (i == vCount - 1 && (i + 1) % count != 0) {
                vHeight = vHeight + childHeight;
            }
        }
        vWidth = childWidth * count;

        width = modeWidth == MeasureSpec.EXACTLY ? sizeWidth : vWidth;
        int height = modeHeight == MeasureSpec.EXACTLY ? sizeHeight : vHeight;

        setMeasuredDimension(width, height);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int child_width = (width - 2 * count * rowMargin) / count;
        int child_height = 0;
        if (vCount > 0) {
            child_height = getChildAt(0).getMeasuredHeight();
        }

        for (int i = 0; i < vCount; i++) {
            View v = getChildAt(i);
            int row = i / count;
            int column = i % count;
            int left = (2 * column + 1) * columnMargin + child_width * column;

            int top = (2 * row + 1) * rowMargin + child_height * row;

            int right = (2 * column + 1) * columnMargin + child_width * (column + 1);

            int bottom = (2 * row + 1) * rowMargin + child_height * (row + 1);


            v.layout(left, top, right, bottom);

        }

    }


}
