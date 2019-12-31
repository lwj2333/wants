package com.lwj.wants.widget.ediitext.util

import android.support.v4.util.ArrayMap
import android.view.MotionEvent
import android.view.View


/**
 * @author by  LWJ
 * @date on 2018/5/9
 * @describe 添加描述
 */
class EditTextFocusManage {
    private var viewMap = ArrayMap<Int, ViewSize>()


    fun addView(list: ArrayList<View>) {
        for (i in 0 until list.size) {
            val v = list[i]
            val a = IntArray(2)
            v.tag = i
            v.getLocationInWindow(a)
            val left = a[0]
            val top = a[1]
            val right = left + v.width
            val bottom = top + v.height
            viewMap[i] = ViewSize(v, left, top, right, bottom)
        }
    }

    private var listener: OnFocusListener? = null
    private var current: Int = -1
    fun setListener(listener: OnFocusListener) {
        this.listener = listener
    }
    fun isAdd() = viewMap.size==0



    fun onEvent(event: MotionEvent,moveTop: Int) {
        val x = event.x.toInt()
        val y = event.y.toInt()

        for (i in 0 until viewMap.size) {
            val size = viewMap[i]

            if (x > size!!.left && x < size.right && y > size.top-moveTop && y < size.bottom-moveTop) {

                if (current != i) {
                    listener?.onFocusListener(viewMap[current]?.v, size.v)
                    current = i
                }
                break
            } else if (i == viewMap.size - 1) {
                listener?.onFocusListener(viewMap[current]?.v, null)
                current = -1
            }
        }

    }

    fun clear() {
        viewMap.clear()
    }

    data class ViewSize(var v: View, var left: Int, var top: Int, var right: Int, var bottom: Int)
    interface OnFocusListener {
        fun onFocusListener(loseFocus: View?, getFocus: View?)
    }
}