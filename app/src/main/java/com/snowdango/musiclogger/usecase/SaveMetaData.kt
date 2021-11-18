package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.toEncodedString
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.ArtworkData
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.KlockLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

object SaveMetaData {

    fun saveMetadata(musicDataBase: MusicDataBase, musicMeta: MusicMeta) = CoroutineScope(Dispatchers.IO).launch {
        val base64Art = musicMeta.artwork.toEncodedString()
        var artworkId = musicDataBase.artworkDao().getArtworkId(base64Art)
        if(artworkId == null){
            artworkId = UUID.randomUUID().toString()
            musicDataBase.artworkDao().insert(ArtworkData(null, artworkId, musicMeta.artwork))
        }
        val tz = DateTimeTz.nowLocal()
        val locale = KlockLocale.default
        musicDataBase.musicMetaDao().insert(
            MusicMetadata(null, musicMeta.title, musicMeta.artist,
                musicMeta.album, musicMeta.mediaId, locale.formatDateTimeMedium.format(tz), artworkId
            ))
    }

}