package com.example.submission1.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submission1.R

class EmailField : AppCompatEditText {
    private lateinit var bgDrawable: Drawable
    var isError: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)

        if (isError) {
            mergeDrawableStates(drawableState, intArrayOf(R.attr.state_error))
        }

        return drawableState
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        refreshDrawableState()
    }

    private fun init() {
        bgDrawable = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        background = bgDrawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isError =
                    TextUtils.isEmpty(s.toString()) || !Patterns.EMAIL_ADDRESS.matcher(s.toString())
                        .matches()

                if (isError) {
                    error = context.getString(R.string.invalid_email)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}