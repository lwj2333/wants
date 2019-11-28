package com.lwj.example.recyclerview

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.recyclerview.adapter.BaseViewHolder
import com.lwj.wants.recyclerview.adapter.RecyclerAdapterMore
import com.lwj.wants.recyclerview.util.RecycleViewDivider
import com.lwj.wants.util.DensityUtil
import kotlinx.android.synthetic.main.activity_simple.*

class MoreActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        initList()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,
            DensityUtil.dip2px(this,2f), ContextCompat.getColor(this, R.color.red)))
        adapter = object : RecyclerAdapterMore<TestModel>(this, list) {
            override fun createView(
                context: Context, inflater: LayoutInflater,
                parent: ViewGroup, viewType: Int
            ): BaseViewHolder {
                var view: View? = null
                when (viewType) {
                    0 -> view = inflater.inflate(R.layout.item_simple, parent, false)
                    1 -> view = inflater.inflate(R.layout.item_more, parent, false)
                }
                return BaseViewHolder(context, view!!, viewType)
            }

            override fun bindView(viewHolder: BaseViewHolder, bean: TestModel, position: Int) {
                when (viewHolder.getType()) {
                    0 -> {
                        val tName: TextView = viewHolder.getView(R.id.tv_name)
                        tName.text = bean.name
                        val tAge: TextView = viewHolder.getView(R.id.tv_age)
                        tAge.text = bean.age.toString()
                    }
                    1 -> {
                        val tName: TextView = viewHolder.getView(R.id.tv_name)
                        tName.text = bean.name
                        val tAge: TextView = viewHolder.getView(R.id.tv_age)
                        tAge.text = bean.age.toString()
                    }
                }
            }
        }
        recycler.setHasFixedSize(true)
        recycler.adapter = adapter

    }

    private val list: ArrayList<TestModel> = ArrayList()
    private var adapter: RecyclerAdapterMore<TestModel>? = null
    private fun initList() {
        list.add(TestModel("张三", 12, 0))
        list.add(TestModel("李四", 12, 1))
        list.add(TestModel("王五", 12, 0))
        list.add(TestModel("赵六", 12, 1))
    }
}