package com.snowdango.musiclogger.repository.db.dao.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

const val MusicMetadataTableName = "history"

@Entity(
    tableName = MusicMetadataTableName,
    indices = [Index(value = ["listening_unix"])]
)
data class MusicMetadata(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title")val title: String,
    @ColumnInfo(name = "artist") val artist: String?,
    @ColumnInfo(name = "album_artist")val albumArtist: String?,
    @ColumnInfo(name = "album")val album: String?,
    @ColumnInfo(name = "media_id")val mediaId: String?,
    @ColumnInfo(name = "listening_unix")val listeningUnix: Long,
    @ColumnInfo(name = "app") val app: String
)
