package com.snowdango.musiclogger.extention

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun String.toBitmap(): Bitmap {
    return Base64.decode(this, Base64.DEFAULT).let {
        BitmapFactory.decodeByteArray(it, 0, it.size)
    }
}