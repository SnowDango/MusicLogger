package com.snowdango.musiclogger.repository.db.dao.entity

import android.graphics.Bitmap
import androidx.room.AutoMigration
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val MusicMetadataTableName = "history"

@Entity(tableName = MusicMetadataTableName)
data class MusicMetadata(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title")val title: String?,
    @ColumnInfo(name = "artist")val artist: String?,
    @ColumnInfo(name = "album")val album: String?,
    @ColumnInfo(name = "media_id")val mediaId: String?,
    @ColumnInfo(name = "listening_date")val listeningDate: String?,
    @ColumnInfo(name = "artwork_id")val artworkId: String?
)
