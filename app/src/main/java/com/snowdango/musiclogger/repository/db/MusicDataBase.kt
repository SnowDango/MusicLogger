package com.snowdango.musiclogger.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.snowdango.musiclogger.repository.db.dao.ArtworkDao
import com.snowdango.musiclogger.repository.db.dao.MusicMetaDao
import com.snowdango.musiclogger.repository.db.dao.MusicMetaWithArtDao
import com.snowdango.musiclogger.repository.db.dao.entity.ArtworkData
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.snowdango.musiclogger.repository.db.dao.type.Converter

@Database(
    entities = [ArtworkData::class, MusicMetadata::class],
    views = [MusicMetaWithArt::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class MusicDataBase: RoomDatabase() {

    abstract fun artworkDao(): ArtworkDao
    abstract fun musicMetaDao(): MusicMetaDao
    abstract fun musicMetaWithArtDao(): MusicMetaWithArtDao

    companion object{
        @Volatile private var INSTANCE: MusicDataBase? = null

        fun getDatabase(context: Context): MusicDataBase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MusicDataBase::class.java, "music_log"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}