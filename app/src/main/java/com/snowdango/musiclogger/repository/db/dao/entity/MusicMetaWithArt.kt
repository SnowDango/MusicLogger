package com.snowdango.musiclogger.repository.db.dao.entity

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "MusicMetaWithArt",
    value = """select history.id,history.title,history.artist,history.album,history.album_artist as albumArtist,history.media_id as mediaId,history.listening_unix as listeningUnix,history.app as appString,artwork_data.artwork_id as artworkId, artwork_data.url as url from `history` left join `artwork_data` on history.album = artwork_data.album and history.album_artist = artwork_data.artist"""
)
data class MusicMetaWithArt(
    val id: Long, // music id
    val title: String, // music title
    val artist: String?, // music artist
    val album: String?, // music album name
    val albumArtist: String?, // music album artist
    val mediaId: String?, // music media id
    val listeningUnix: Long, // listening date
    val appString: String,
    val artworkId: String?, // artwork id (ex. artworkId.jpg
    val url: String? // artwork url
)
