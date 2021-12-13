package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata

@Dao
interface MusicMetaDao {

    @Insert
    fun insert(musicMetadata: MusicMetadata): Long

    @Query("select * from `history` order by listening_unix desc limit :offset,100")
    fun getDataLimit100(offset: Int): List<MusicMetadata>

    @Query("delete from `history` where id = (select min(id) from `history`) ")
    fun getMostOldDelete()

    @Update
    fun update(musicMetadata: MusicMetadata)
}