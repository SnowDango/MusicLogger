package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt

@Dao
interface MusicMetaWithArtDao {

    @Query("select * from MusicMetaWithArt limit :offset,100")
    fun getCompleteMusicMetaLimit100(offset: Int): List<MusicMetaWithArt>

}