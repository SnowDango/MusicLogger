package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt

@Dao
interface MusicMetaWithArtDao {

    @Query("select * from MusicMetaWithArt order by id desc limit :offset,100")
    fun getCompleteMusicMetaLimit100(offset: Int): List<MusicMetaWithArt>

    @Query("select * from MusicMetaWithArt order by id desc")
    fun get(): List<MusicMetaWithArt>

}