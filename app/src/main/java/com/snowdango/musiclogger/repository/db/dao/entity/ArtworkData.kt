package com.snowdango.musiclogger.repository.db.dao.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

const val ArtworkDataTableName = "artwork_data"

@Entity(
    tableName = ArtworkDataTableName,
    indices = [Index(value = ["album", "artist"], unique = true)]
)
data class ArtworkData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "artwork_id") val imageId: String?,
    @ColumnInfo(name = "album") val album: String?,
    @ColumnInfo(name = "artist") val artist: String?
)
