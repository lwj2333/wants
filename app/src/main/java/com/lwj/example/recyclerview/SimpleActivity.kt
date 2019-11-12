package com.lwj.example.recyclerview

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.recyclerview.adapter.BaseViewHolder
import com.lwj.wants.recyclerview.adapter.RecyclerAdapterList
import kotlinx.android.synthetic.main.activity_simple.*

class SimpleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        initList()
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = object : RecyclerAdapterList<TestModel>(this, list, R.layout.item_simple) {
            override fun bindView(viewHolder: BaseViewHolder, bean: TestModel, position: Int) {
                val tName: TextView = viewHolder.getView(R.id.tv_name)
                tName.text = bean.name
                val tAge: TextView = viewHolder.getView(R.id.tv_age)
                tAge.text = bean.age.toString()
            }
        }
        recycler.setHasFixedSize(true)
        recycler.adapter = adapter

    }
private val TAG ="SimpleActivity"
    private val list: ArrayList<TestModel> = ArrayList()
    private var adapter: RecyclerAdapterList<TestModel>? = null
    private fun initList() {
        list.add(TestModel("张三", 12))
        list.add(TestModel("李四", 13))
        list.add(TestModel("王五", 14))
        list.add(TestModel("赵六", 15))
    }
}