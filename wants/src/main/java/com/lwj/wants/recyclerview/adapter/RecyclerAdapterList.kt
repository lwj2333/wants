package com.lwj.wants.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.ArrayList

abstract class RecyclerAdapterList<T> : RecyclerView.Adapter<BaseViewHolder> {
    private var list: List<T>? = null
    private var context: Context? = null
    private var layoutID: Int = -1
    private var mInflater: LayoutInflater? = null

    constructor(context: Context, list: List<T>, layoutID: Int) : super() {
        this.list = list
        this.context = context
        this.layoutID = layoutID
        this.mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = mInflater!!.inflate(layoutID, parent, false)
        return BaseViewHolder(context!!, view)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int
    ) {
        bindView(holder, list!![position], position)
    }

    override fun getItemCount(): Int {
        return if (list == null) {
            0
        } else {
            list!!.size
        }
    }

    abstract fun bindView(viewHolder: BaseViewHolder, bean: T, position: Int)
}