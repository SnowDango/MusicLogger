package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.snowdango.musiclogger.repository.db.dao.entity.ArtworkData

@Dao
interface ArtworkDao {

    @Insert
    fun insert(artworkData: ArtworkData)

    @Query("select * from `artwork_data` limit :offset,100")
    fun getArtworkDataLimit100(offset: Int): List<ArtworkData>

    @Query("select artwork_id from `artwork_data` where album=:album and artist=:albumArtist")
    fun getArtworkId(album: String?, albumArtist: String?): String?

    @Query("select * from `artwork_data` where album=:album and artist=:albumArtist")
    fun getArtworkData(album: String?, albumArtist: String?): ArtworkData?

    @Query("update `artwork_data` set url=:url, artwork_id=null where id = :id")
    fun updateUrl(id: Long, url: String)

}