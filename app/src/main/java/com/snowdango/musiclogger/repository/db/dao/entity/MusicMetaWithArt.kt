package com.snowdango.musiclogger.repository.db.dao.entity

import android.graphics.Bitmap
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "MusicMetaWithArt",
    value = """ select history.id, history.title, history.artist, history.album, history.album_artist as albumArtist, history.media_id as mediaId, history.listening_date as listeningDate, artwork_data.artwork_id as artworkId
from `history` left join `artwork_data` on history.album = artwork_data.album and history.album_artist = artwork_data.artist"""
)
data class MusicMetaWithArt(
    val id: Long, // music id
    val title: String, // music title
    val artist: String?, // music artist
    val album: String?, // music album name
    val albumArtist: String?, // music album artist
    val mediaId: String?, // music media id
    val listeningDate: Long, // listening date
    val artworkId: String?, // artwork id (ex. artworkId.jpg
)
