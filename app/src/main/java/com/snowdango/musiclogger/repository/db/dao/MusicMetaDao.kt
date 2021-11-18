package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata

@Dao
interface MusicMetaDao {

    @Insert
    fun insert(musicMetadata: MusicMetadata)

    @Query("select * from `history` limit 100")
    fun getDataLimit100(): List<MusicMetadata>

    @Query("delete from `history` where id = (select min(id) from `history`) ")
    fun getMostOldDelete()

}