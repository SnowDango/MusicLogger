package com.snowdango.musiclogger.repository.db.dao.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val ArtworkDataTableName = "artwork_data"

@Entity(
    tableName = ArtworkDataTableName
)
data class ArtworkData(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "artwork_id") val imageId: String,
    @ColumnInfo(name = "artwork") val artwork: Bitmap
)
