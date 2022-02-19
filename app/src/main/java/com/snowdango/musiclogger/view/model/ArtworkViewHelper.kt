package com.snowdango.musiclogger.view.model

import androidx.lifecycle.MutableLiveData
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.repository.api.ApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.io.path.Path

class ArtworkViewHelper {

    val artworkPath: MutableLiveData<String?> = MutableLiveData<String?>().also {
        it.value = null
    }

    fun fetchArtwork(artworkViewData: ArtworkView.ArtworkViewData) {
        artworkViewData.mediaId?.let { getSongData(it) }
    }

    private fun getSongData(mediaId: String) = CoroutineScope(Dispatchers.Default).launch {
        val apiResult = try {
            ApiProvider.appleApi.getSongInfo(mediaId).execute()
        } catch (e: Exception) {
            null
        }
        apiResult?.let {
            it.body()?.appleSearchResults?.let { list ->
                if (list.isNotEmpty()) {
                    list[0].artworkUrl100?.let { artworkUrl100 ->
                        artworkPath.postValue(generateArtworkUrl(artworkUrl100))
                    }
                }
            }
        }
    }

    private fun generateArtworkUrl(baseUrl: String): String {
        val imagePath = Path(baseUrl)
        return baseUrl.replace(imagePath.fileName.toString(), "${DETAIL_IMAGE_SIZE}x$DETAIL_IMAGE_SIZE.jpg")
    }
}