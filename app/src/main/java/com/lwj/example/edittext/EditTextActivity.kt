package com.lwj.example.edittext

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.lwj.example.BaseActivity
import com.lwj.example.R
import com.lwj.wants.widget.ediitext.ClearEditText
import com.lwj.wants.widget.ediitext.util.EditTextFocusManage
import kotlinx.android.synthetic.main.activity_edittext.*

class EditTextActivity : BaseActivity() {
    private var manage: EditTextFocusManage? = null
    private val listView = ArrayList<View>()
    private var moveTop: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edittext)
        listView.add(edit_1)
        listView.add(edit_2)
        manage = EditTextFocusManage()
        manage!!.setListener(object : EditTextFocusManage.OnFocusListener {
            override fun onFocusListener(loseFocus: View?, getFocus: View?) {
                when {
                    getFocus != null -> {
                    }
                    loseFocus!=null -> {
                        clearFocus(loseFocus)
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(loseFocus.windowToken, 0)
                    }
                }
            }
        })

        edit_1.getEditText().setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    Log.i(TAG, "ProductActivity_initAction:$   搜索")
                }
            }
            return@setOnEditorActionListener true
        }
    }
     private val TAG = "EditTextActivity"
    fun clearFocus(view:View){
        when(view.tag){
            0->{
                (view as ClearEditText).clearFocus()
            }
            1->{
                (view as EditText).clearFocus()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (manage!!.isAdd()) {
            manage!!.addView(listView)
        }
        if (event?.action == MotionEvent.ACTION_DOWN) {
            manage!!.onEvent(event, moveTop)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        manage!!.clear()
    }
}