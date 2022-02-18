package com.snowdango.musiclogger.glide

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.snowdango.musiclogger.IMAGE_SIZE
import com.snowdango.musiclogger.view.common.glide.ImageCrop


fun RequestManager.customRequestBuilder(
    cropType: ImageCrop,
    isDetail: Boolean
): RequestBuilder<Drawable> {
    return this.asDrawable().apply(customRequestOptions(cropType, isDetail)).override(IMAGE_SIZE)
}

@SuppressLint("CheckResult")
fun customRequestOptions(cropType: ImageCrop, isDetail: Boolean): RequestOptions {
    val requestOptions = RequestOptions().also {
        if (isDetail) {
            it.override(DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_SIZE)
        } else {
            it.override(IMAGE_SIZE, IMAGE_SIZE)
        }
        it.diskCacheStrategy(DiskCacheStrategy.ALL)
        if (cropType == ImageCrop.Circle) {
            it.circleCrop()
        } else if (cropType == ImageCrop.RoundedCorner) {
            val transformation = MultiTransformation(CenterCrop(), RoundedCorners(80))
            it.transform(transformation)
        }
    }
    return requestOptions
}