package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt


@Dao
interface AlbumWithArtDao {

    @Query("select * from AlbumWithArt group by album, appString order by album desc limit :offset,100")
    fun getAlbumWithArtLimit100(offset: Int): List<AlbumWithArt>

}