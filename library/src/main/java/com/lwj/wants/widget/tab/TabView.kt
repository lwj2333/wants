package com.lwj.wants.widget.tab

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
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
       radio.setPadding(DensityUtil.dip2px(context,leftP),DensityUtil.dip2px(context,topP),
           DensityUtil.dip2px(context,rightP),DensityUtil.dip2px(context,bottomP))

        return radio
    }

    private val TAG ="TabView"
    private var drawableSize :Float =50f
    private fun createDrawable(drawableID:Int):Drawable?{
         if (drawableID==0){
             return null
         }
          Log.i(TAG,"TabView: $drawableID  ")
        val drawable = ContextCompat.getDrawable(context,drawableID)

       drawable?.setBounds(0,0,
           DensityUtil.dip2px(context,drawableSize),DensityUtil.dip2px(context,drawableSize))
        return drawable
    }
    fun setDrawableSize(size:Float){
        drawableSize =size
    }
    private var leftP :Float= 0f
    private var rightP:Float =0f
    private var topP :Float=0f
    private var bottomP:Float=0f

    fun setPadding(p :Float){
        setPadding(p,p,p,p)
    }
    fun setPadding(l :Float,t:Float,r:Float,b:Float){
         leftP =l
         topP=t
         rightP=r
         bottomP=b
    }
}