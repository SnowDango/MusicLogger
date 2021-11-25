package com.snowdango.musiclogger.extention

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toEncodedString(): String {
    val bos = ByteArrayOutputStream().also {
        if (!compress(Bitmap.CompressFormat.PNG, 50, it)) return ""
    }
    return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
}

fun Bitmap.toJpegByteArray(): ByteArray{
    val bos = ByteArrayOutputStream().also {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return bos.toByteArray()
}