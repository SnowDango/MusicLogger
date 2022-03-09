package com.snowdango.musiclogger.view.model

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.glide.ImageCrop
import com.snowdango.musiclogger.glide.customRequestBuilder
import com.snowdango.musiclogger.repository.api.AppleApiProvider
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
        val artworkUrl = artworkViewData.mediaId?.let { getSongData(it) }
        CoroutineScope(Dispatchers.Main).launch {
            loadArtwork(artworkUrl, imageCrop)
        }
    }
}

private fun getSongData(mediaId: String): String? {
    val apiResult = try {
        AppleApiProvider.appleApi.getSongInfo(mediaId).execute()
    } catch (e: Exception) {
        null
    }
    apiResult?.let {
        it.body()?.appleSearchResults?.let { list ->
            if (list.isNotEmpty()) {
                list[0].artworkUrl100?.let { artworkUrl100 ->
                    Timber.d("artwork for media id $artworkUrl100")
                    return generateArtworkUrl(artworkUrl100)
                }
            }
        }
    }
    return null
}

private fun generateArtworkUrl(baseUrl: String): String {
    val imagePath = Path(baseUrl)
    return baseUrl.replace(imagePath.fileName.toString(), "${DETAIL_IMAGE_SIZE}x$DETAIL_IMAGE_SIZE.jpg")
}