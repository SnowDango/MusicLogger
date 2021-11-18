package com.snowdango.musiclogger.repository.db.dao.type

import android.graphics.Bitmap
import androidx.room.TypeConverter
import com.snowdango.musiclogger.extention.toBitmap
import com.snowdango.musiclogger.extention.toEncodedString

class Converter {

    @TypeConverter
    fun Bitmap.convertString(): String = this.toEncodedString()

    @TypeConverter
    fun String.convertBitmap(): Bitmap = this.toBitmap()

}