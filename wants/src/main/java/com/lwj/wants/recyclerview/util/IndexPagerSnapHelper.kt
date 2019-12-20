package com.lwj.wants.recyclerview.util

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView


/**
 * @author by  LWJ
 * @date on 2018/6/29
 * @describe 添加描述
 * @org  http://www.gdjiuji.com(广东九极生物科技有限公司)
 */
class IndexPagerSnapHelper : PagerSnapHelper() {
    private var listener :PagerChangeListener?=null
    fun setPagerChangeListener(listener :PagerChangeListener){
        this.listener = listener
    }
    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {
        val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if (listener!=null){
            listener!!.onPagerChange(position)
        }
        return position
    }
    interface PagerChangeListener{
        fun onPagerChange(position:Int)
    }
}