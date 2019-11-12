package com.lwj.wants.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.reflect.Field
import java.util.ArrayList

abstract class RecyclerAdapterMore<T : Any> : RecyclerView.Adapter<BaseViewHolder>{
    private var list: List<T>? = null
    private var context: Context?=null
    private var mInflater: LayoutInflater? =null
    constructor(context: Context, list:List<T>) : super() {
        this.list=list
        this.context = context
        this.mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return createView(context!!, mInflater!!, parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindView(holder, list!![position], position)
    }


    override fun getItemViewType(position: Int): Int {
        var index = -1
        try {
            val f:Field = list!![position].javaClass.getDeclaredField("type")
            f.isAccessible = true
            index = f.getInt(list!![position])
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return index
    }

    override fun getItemCount(): Int {
        return if (list == null) {
            0
        } else {
            list!!.size
        }
    }
    abstract fun bindView(viewHolder: BaseViewHolder, bean: T, position:Int )
    abstract fun createView(context: Context,inflater: LayoutInflater,
                             parent:ViewGroup,viewType:Int): BaseViewHolder
}