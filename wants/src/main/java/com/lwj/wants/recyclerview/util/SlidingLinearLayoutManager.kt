package com.lwj.wants.recyclerview.util

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

class SlidingLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var isScrollEnabled = true
     fun setScrollEnabled(flag: Boolean ) {
        this.isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}