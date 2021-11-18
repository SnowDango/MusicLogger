package com.snowdango.musiclogger.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.snowdango.musiclogger.repository.db.dao.entity.ArtworkData

@Dao
interface ArtworkDao {

    @Insert
    fun insert(artworkData: ArtworkData)

    @Query("select * from `artwork_data` limit 100")
    fun getArtworkDataLimit100(): List<ArtworkData>

    @Query("select artwork_id from `artwork_data` where artwork = :artwork")
    fun getArtworkId(artwork: String): String?

}