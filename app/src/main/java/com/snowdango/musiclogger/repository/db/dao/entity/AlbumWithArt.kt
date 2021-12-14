package com.snowdango.musiclogger.repository.db.dao.entity

import androidx.room.DatabaseView


@DatabaseView(
    viewName = "AlbumWithArt",
    value = """select history.album as album, history.album_artist as albumArtist, history.app as appString, artwork_data.artwork_id as artworkId from `history` left join `artwork_data` on history.album = artwork_data.album and history.album_artist = artwork_data.artist"""
)
data class AlbumWithArt(
    val album: String?,
    val albumArtist: String?,
    val appString: String?,
    val artworkId: String?
)
