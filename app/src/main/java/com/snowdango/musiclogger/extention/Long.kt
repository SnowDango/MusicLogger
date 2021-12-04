package com.snowdango.musiclogger.extention

import android.annotation.SuppressLint
import com.soywiz.klock.KlockLocale
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.fromUnix2String(): String {
    val date = Date(this)
    val local = KlockLocale.default
    val format = SimpleDateFormat(local.formatDateTimeMedium.toString())
    return format.format(date)
}