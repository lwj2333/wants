package com.lwj.wants.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 *  Created by lwj on 2019/11/8
 *  Describe 添加描述
 */
class BaseViewHolder : RecyclerView.ViewHolder {
    private var type: Int = 0
    private var mContext: Context? = null
    private val views: SparseArray<View?> = SparseArray()

    constructor( mContext: Context?,itemView: View, type: Int) : super(itemView) {
        this.type = type
        this.mContext = mContext
    }
    constructor(mContext: Context?,itemView: View):super(itemView){
        this.mContext = mContext
    }

    @Suppress("UNCHECKED_CAST")
    fun  <T : View> getView(id: Int): T {
        var v= views[id]
      if (v==null){
          v = itemView.findViewById(id)
          views.put(id,v)
        }
        return v as T
    }

    fun getType(): Int {
        return type
    }

    fun setType(type: Int) {
        this.type = type
    }
}