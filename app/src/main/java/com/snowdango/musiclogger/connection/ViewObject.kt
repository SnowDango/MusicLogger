package com.snowdango.musiclogger.connection

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData

object ViewObject {

    val art = MutableLiveData<Bitmap?>().apply {
        value = null
    }

}