package com.lwj.wants.widget.ediitext

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.lwj.wants.R
import android.text.InputFilter
import android.content.res.ColorStateList
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log

import android.util.TypedValue

import android.view.inputmethod.EditorInfo



class ClearEditText : ConstraintLayout {

    constructor(context: Context?) : super(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
        initAttrs(attrs)
    }


    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray =
            context?.obtainStyledAttributes(attrs, R.styleable.ClearEditText)
        if (typedArray != null) {
            val count = typedArray.indexCount
            for (i in 0 until count) {
                when (val attr = typedArray.getIndex(i)) {
                    R.styleable.ClearEditText_android_autoLink -> {
                        val autoLink = typedArray.getInt(attr, 0)
                        editText!!.autoLinkMask = autoLink
                    }
                    R.styleable.ClearEditText_android_drawableRight -> {
                        val draw = typedArray.getDrawable(attr)
                        img!!.setImageDrawable(draw)
                    }
                    R.styleable.ClearEditText_android_drawablePadding -> {
                        val padding = typedArray.getDimensionPixelSize(attr, 0)
                        img!!.setPadding(padding, 0, 0, 0)
                    }
                    R.styleable.ClearEditText_android_maxLines -> {
                        val lines = typedArray.getInt(attr, -1)
                        editText!!.maxLines = lines
                    }
                    R.styleable.ClearEditText_android_hint -> {
                        val hint = typedArray.getText(attr)
                        editText!!.hint = hint
                    }
                    R.styleable.ClearEditText_android_text -> {
                        val text = typedArray.getText(attr)
                        editText!!.setText(text)
                    }
                    R.styleable.ClearEditText_android_enabled -> {
                        val enabled = typedArray.getBoolean(attr, isEnabled)
                        editText!!.isEnabled = enabled
                    }
                    R.styleable.ClearEditText_android_ellipsize -> {
                        var ellipsize = typedArray.getInt(attr, -1)
                        if (editText!!.maxLines == 1 && editText!!.keyListener == null && ellipsize < 0) {
                            ellipsize = 3 // END
                        }
                        when (ellipsize) {
                            1 -> editText!!.ellipsize = TextUtils.TruncateAt.START
                            2 -> editText!!.ellipsize = TextUtils.TruncateAt.MIDDLE
                            3 -> editText!!.ellipsize = TextUtils.TruncateAt.END
                            4 -> editText!!.ellipsize = TextUtils.TruncateAt.MARQUEE
                            else -> {
                            }
                        }
                    }
                    R.styleable.ClearEditText_android_cursorVisible -> {
                        if (!typedArray.getBoolean(attr, true)) {
                            editText!!.isCursorVisible = false
                        }
                    }
                    R.styleable.ClearEditText_android_maxLength -> {
                        val maxLength = typedArray.getInt(attr, -1)
                        if (maxLength >= 0) {
                            editText!!.filters = arrayOf<InputFilter>(
                                InputFilter.LengthFilter(
                                    maxLength
                                )
                            )
                        } else {
                            editText!!.filters = arrayOfNulls(0)
                        }
                    }
                    R.styleable.ClearEditText_android_textColor -> {
                        val textColor: ColorStateList? = typedArray.getColorStateList(attr)
                        editText!!.setTextColor(textColor)
                    }
                    R.styleable.ClearEditText_android_textColorHint -> {
                        val textColorHint: ColorStateList? = typedArray.getColorStateList(attr)
                        editText!!.setHintTextColor(textColorHint)
                    }
                    R.styleable.ClearEditText_android_textColorLink -> {
                        val textColorLink: ColorStateList? = typedArray.getColorStateList(attr)
                        editText!!.setLinkTextColor(textColorLink)
                    }
                    R.styleable.ClearEditText_android_textSize -> {
                        val textSize = typedArray.getDimensionPixelSize(attr, 16)
                        editText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                    }
                    R.styleable.ClearEditText_android_inputType -> {
                        val inputType = typedArray.getInt(attr, EditorInfo.TYPE_NULL)
                        editText!!.inputType = inputType
                    }
                    R.styleable.ClearEditText_android_gravity -> {
                        val gravity = typedArray.getInt(attr, -1)
                        editText!!.gravity = gravity
                    }
                    R.styleable.ClearEditText_android_imeOptions->{
                       val imeOptions = typedArray.getInteger(
                           attr,
                            EditorInfo.IME_ACTION_NONE
                        )
                        editText!!.imeOptions = imeOptions
                    }
                    R.styleable.ClearEditText_background -> {
                        val background = typedArray.getResourceId(attr, R.color.white)
                        editText!!.setBackgroundResource(background)
                    }
                    R.styleable.ClearEditText_showRightDrawable -> {
                        val show = typedArray.getBoolean(attr, true)
                        img!!.visibility = if (show) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                }
            }
        }

        typedArray?.recycle()
    }

    private var editText: EditText? = null
    private var img: ImageView? = null
    private var textWatcher: TextWatcher? = null

    private fun initView() {

        setView()
        img!!.setOnClickListener {
            editText!!.text = null
        }
        editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (editText!!.isFocused && s?.length ?: 0 > 0) {
                    showIcon(true)
                } else {
                    showIcon(false)
                }
                textWatcher?.afterTextChanged(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textWatcher?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length ?: 0 > 0) {
                    showIcon(true)
                } else {
                    showIcon(false)
                }
                textWatcher?.onTextChanged(s, start, before, count)
            }
        })
        editText!!.setOnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    if (!TextUtils.isEmpty(editText!!.text))
                        showIcon(true)
                }
                false -> {
                    showIcon(false)
                }
            }
        }
    }
      private val TAG = "ClearEditText"
    private var changeConstraintSet: ConstraintSet? = null
    /**
     * 动态添加控件
     */
    private fun setView() {


        editText = EditText(context)
        editText!!.id = View.generateViewId()


        img = ImageView(context)
        img!!.id = View.generateViewId()
        img!!.setImageResource(R.drawable.ic_action_delete)
        img!!.scaleType = ImageView.ScaleType.CENTER

        addView(editText)
        addView(img)
        val c = ConstraintSet()
        c.clone(this)
        c.constrainWidth(editText!!.id, 0)
        c.constrainHeight(editText!!.id, 0)
        c.setDimensionRatio(img!!.id, "h,1:1") //宽高比 1:1
        c.connect(img!!.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        c.connect(img!!.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        c.connect(img!!.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        c.constrainWidth(editText!!.id, ConstraintSet.MATCH_CONSTRAINT)
        c.constrainHeight(editText!!.id, ConstraintSet.WRAP_CONTENT)
        c.connect(editText!!.id, ConstraintSet.END, img!!.id, ConstraintSet.START)
        c.connect(editText!!.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        c.connect(editText!!.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        c.connect(
            editText!!.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        // c.setMargin(editText!!.id,ConstraintSet.TOP,DensityUtil.dip2px(context,2f))
        c.applyTo(this)
        changeConstraintSet = ConstraintSet()
        changeConstraintSet!!.clone(c)
    }
    private var flag = 0
    private fun setParams(){
        if (flag==0){
            flag += 1
            changeConstraintSet!!.constrainHeight(editText!!.id, ConstraintSet.MATCH_CONSTRAINT)
            changeConstraintSet!!.applyTo(this)
            img!!.visibility = View.GONE // editText发生变化 需要重新设置img的visibility
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST -> {
            }
            MeasureSpec.EXACTLY -> {
                setParams()
            }
            else -> {
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    /**
     * 返回EditText中的文本数据
     */
    fun getText(): Editable? {
        return editText!!.text
    }

    /**
     * 设置文本到EditText中
     */
    fun setText(charSequence: CharSequence) {
        editText!!.setText(charSequence)
    }

    /**
     * 设置EditText的选中
     */
    fun setSelection(index: Int) {
        editText!!.setSelection(index)
    }

    /**
     * 为EditText设置过滤器
     */
    fun setFilters(filters: Array<InputFilter>) {
        editText!!.filters = filters
    }

    fun getEditText(): EditText {
        return editText!!
    }

    override fun clearFocus() {
        editText!!.clearFocus()
        super.clearFocus()
    }


    private fun showIcon(show: Boolean) {
        img!!.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        this.textWatcher = textWatcher
    }
}