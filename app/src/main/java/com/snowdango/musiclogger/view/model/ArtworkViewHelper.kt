package com.snowdango.musiclogger.view.model

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.domain.session.MusicApp
import com.snowdango.musiclogger.glide.ImageCrop
import com.snowdango.musiclogger.glide.customRequestBuilder
import com.snowdango.musiclogger.repository.api.AppleApiProvider
import com.snowdango.musiclogger.repository.api.SpotifyApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.io.path.Path

fun ArtworkView.requestBuilder(
    requestManager: RequestManager, crop: ImageCrop,
    failedFn: (
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ) -> Unit,
    readyFn: (
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ) -> Unit
): RequestBuilder<Drawable> {
    return requestManager.customRequestBuilder(crop).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            failedFn(e, model, target, isFirstResource)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            readyFn(resource, model, target, dataSource, isFirstResource)
            return false
        }
    })
}

fun ArtworkView.apiFetchArtwork(artworkViewData: ArtworkView.ArtworkViewData, imageCrop: ImageCrop) {
    CoroutineScope(Dispatchers.Default).launch {
        Timber.d("${artworkViewData.mediaId} is failed")
        val artworkUrl = artworkViewData.mediaId?.let { getSongData(it, artworkViewData.appString) }
        CoroutineScope(Dispatchers.Main).launch {
            loadArtwork(artworkUrl, imageCrop)
        }
    }
}

private suspend fun getSongData(mediaId: String, appString: String): String? {
    val callResult = try {
        when (appString) {
            MusicApp.AppleMusic.string -> callAppleArtwork(mediaId)
            MusicApp.Spotify.string -> callSpotifyArtwork(mediaId)
            else -> null
        }
    } catch (e: Exception) {
        null
    }
    return callResult
}

private fun callAppleArtwork(mediaId: String): String? {
    val apiResult = try {
        AppleApiProvider.appleApi.getSongInfo(mediaId).execute()
    } catch (e: Exception) {
        null
    }
    apiResult?.let {
        it.body()?.appleSearchResults?.let { list ->
            if (list.isNotEmpty()) {
                list[0].artworkUrl100?.let { artworkUrl100 ->
                    Timber.d("artwork for apple media id $artworkUrl100")
                    return generateAppleArtworkUrl(artworkUrl100)
                }
            }
        }
    }
    return null
}

private suspend fun callSpotifyArtwork(mediaId: String): String? {
    val apiResult = try {
        SpotifyApiProvider.authorizedSpotifyApi()!!.getTrack(mediaId).execute()
    } catch (e: Exception) {
        null
    }
    apiResult?.let {
        it.body()?.album?.let { album ->
            if (!album.images.isNullOrEmpty()) {
                Timber.d("artwork for spotify media id ${album.images[0].url}")
                return album.images[0].url
            }
        }
    }
    return null
}


private fun generateAppleArtworkUrl(baseUrl: String): String {
    val imagePath = Path(baseUrl)
    return baseUrl.replace(imagePath.fileName.toString(), "${DETAIL_IMAGE_SIZE}x$DETAIL_IMAGE_SIZE.jpg")
}