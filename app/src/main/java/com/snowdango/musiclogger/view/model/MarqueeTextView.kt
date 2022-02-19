package com.snowdango.musiclogger.view.model

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatTextView


class MarqueeTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
    }

    override fun isFocused(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect)
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (hasWindowFocus) super.onWindowFocusChanged(hasWindowFocus)
    }

}