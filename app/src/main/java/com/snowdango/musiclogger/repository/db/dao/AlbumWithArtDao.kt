package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt


@Dao
interface AlbumWithArtDao {

    @Query("select * from AlbumWithArt group by album, appString order by album asc limit :offset,100")
    fun getAlbumWithArtLimit100(offset: Int): List<AlbumWithArt>

    @Query("select * from AlbumWithArt where url is null group by album, appString order by album asc")
    fun getUrlIsNull(): List<AlbumWithArt>

}