package com.snowdango.musiclogger.repository.db.dao.entity

import android.graphics.Bitmap
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "MusicMetaWithArt",
    value = """
        select history.title, history.artist, history.album,
        history.media_id AS mediaId, history.listening_date AS listeningDate, artwork_data.artwork AS artwork
         from `history` inner join `artwork_data` on history.artwork_id = artwork_data.artwork_id
    """
)
data class MusicMetaWithArt(
    val title: String?,
    val artist: String?,
    val album: String?,
    val mediaId: String?,
    val listeningDate: String?,
    val artwork: Bitmap
)
