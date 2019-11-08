package com.lwj.wants.recyclerview

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 *  Created by lwj on 2019/11/8
 *  Describe 添加描述 
 */
class ViewHolder : RecyclerView.ViewHolder{
    private var type: Int = 0
    private var mContext: Context? = null
    private val views : SparseArray<View> = SparseArray()

    constructor(itemView: View, type: Int, mContext: Context?) : super(itemView) {
        this.type = type
        this.mContext = mContext
    }
  fun <T : View> getView(id:Int):View{
      return itemView.findViewById(id) as T
  }
    fun getType(): Int {
        return type
    }

    fun setType(type: Int) {
        this.type = type
    }
}