package com.snowdango.musiclogger.repository.db.dao.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class MusicMetadata(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "title")val title: String?,
    @ColumnInfo(name = "artist")val artist: String?,
    @ColumnInfo(name = "album")val album: String?,
    @ColumnInfo(name = "media-id")val mediaId: String?,
    @ColumnInfo(name = "artwork-id")val artworkId: String?
)
