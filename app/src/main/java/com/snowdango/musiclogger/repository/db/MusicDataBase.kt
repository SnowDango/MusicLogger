package com.snowdango.musiclogger.repository.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.snowdango.musiclogger.repository.db.dao.ArtworkDao
import com.snowdango.musiclogger.repository.db.dao.MusicMetaDao
import com.snowdango.musiclogger.repository.db.dao.MusicMetaWithArtDao
import com.snowdango.musiclogger.repository.db.dao.entity.ArtworkData
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata

@Database(
    entities = [ArtworkData::class, MusicMetadata::class],
    views = [MusicMetaWithArt::class],
    version = 2,
    exportSchema = false
)
abstract class MusicDataBase : RoomDatabase() {

    abstract fun artworkDao(): ArtworkDao
    abstract fun musicMetaDao(): MusicMetaDao
    abstract fun musicMetaWithArtDao(): MusicMetaWithArtDao

}