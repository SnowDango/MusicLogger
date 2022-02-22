package com.snowdango.musiclogger.view.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.RequestManager
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.glide.CustomGlide
import com.snowdango.musiclogger.glide.ImageCrop
import com.snowdango.musiclogger.glide.customRequestBuilder


class AppIconView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val appIconImageView: ImageView
    private val requestManager: RequestManager

    init {
        LayoutInflater.from(context).inflate(R.layout.app_icon_view, this, true)
        appIconImageView = findViewById(R.id.appIconImageView)
        requestManager = CustomGlide.with(context)
    }

    fun update(appName: String) {
        val appIconPath = "file:///android_asset/${appName}.png"
        requestManager.customRequestBuilder(ImageCrop.Circle).load(appIconPath).into(appIconImageView)
    }

    fun clear() {
        requestManager.clear(appIconImageView)
    }

}