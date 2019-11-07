package com.lwj.wants.widget.tab

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup

import com.lwj.wants.util.DensityUtil
import com.lwj.wants.widget.tab.model.RadioItem
import java.util.ArrayList

class TabView : RadioGroup {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
   private val data :ArrayList<RadioItem> = ArrayList()
    fun addData(list :ArrayList<RadioItem>?) {

             if (list==null||list.size==0){
                 return
             }
            data.addAll(list)
        for (i in 0 until list.size){
            val button = createButton(list[i],i)
            this.addView(button)
        }

    }

    private fun createButton(bean: RadioItem, id:Int):RadioButton{
        val radio = RadioButton(context)
        radio.setBackgroundResource(bean.backgroundID)
        radio.buttonDrawable = null

      val drawable = createDrawable(bean.drawableID)
      radio.setCompoundDrawables(null,drawable,null,null)
     // radio.setButtonDrawable(bean.drawableID)

        radio.text = bean.text
        radio.gravity = Gravity.CENTER
        radio.id=id
        radio.setTextColor( ContextCompat.getColorStateList(context,bean.textColorID))
        val params = LayoutParams(0,LayoutParams.WRAP_CONTENT)
        params.weight=1.0f
        radio.layoutParams =params

        return radio
    }
    private var drawableSize :Float =50f
    private fun createDrawable(drawableID:Int):Drawable?{
         if (drawableID==0){
             return null
         }
        val drawable = ContextCompat.getDrawable(context,drawableID)
       drawable?.setBounds(0,0,
           DensityUtil.dip2px(context,drawableSize),DensityUtil.dip2px(context,drawableSize))
        return drawable
    }
    fun setDrawableSize(size:Float){
        drawableSize =size
    }
}