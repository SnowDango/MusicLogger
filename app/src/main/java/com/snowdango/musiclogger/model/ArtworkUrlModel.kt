package com.snowdango.musiclogger.model

import com.snowdango.musiclogger.domain.session.MusicApp
import com.snowdango.musiclogger.usecase.api.FetchAppleMusicData
import com.snowdango.musiclogger.usecase.artwork.SaveArtworkUrl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.io.path.Path

class ArtworkUrlModel : KoinComponent {

    val fetchAppleMusicData by inject<FetchAppleMusicData>()
    val saveArtworkUrl by inject<SaveArtworkUrl>()

    suspend fun setArtworkUrl(appString: String, mediaId: String, artworkDataId: Long) {
        when (appString) {
            MusicApp.AppleMusic.string -> setAppleMusicArtwork(mediaId, artworkDataId)
            else -> {}
        }
    }

    suspend fun setAppleMusicArtwork(mediaId: String, artworkDataId: Long) {
        val musicData = fetchAppleMusicData.getMusicForMediaId(mediaId)
        musicData?.artworkUrl100?.let {
            val fileName = Path(it).fileName.toString()
            val baseUrl = it.replace(fileName, "")
            saveArtworkUrl.saveArtworkUrl(baseUrl, artworkDataId)
        }
    }

}