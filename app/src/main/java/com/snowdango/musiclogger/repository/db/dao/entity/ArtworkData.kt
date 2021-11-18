package com.snowdango.musiclogger.repository.db.dao.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "artwork-data")
data class ArtworkData(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "artwork-id") val imageId: String,
    @ColumnInfo(name = "artwork") val artwork: Bitmap
)
