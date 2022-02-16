package com.snowdango.musiclogger.view.common.glide

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.IMAGE_SIZE
import com.snowdango.musiclogger.repository.api.ApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import java.nio.file.Paths
import kotlin.io.path.Path

@Composable
fun ArtworkGlide(
    url: String?,
    artworkId: String?,
    isDetail: Boolean = false,
    mediaId: String,
    appString: String,
    cropType: ImageCrop = ImageCrop.Circle,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = "",
    context: Context = get(),
    modifier: Modifier = Modifier
) {
    CustomGlide(
        imageModel = getArtworkPath(url, artworkId, isDetail, context),
        failed = { ArtworkFailed(mediaId, cropType, isDetail, appString) },
        cropType = cropType,
        alignment = alignment,
        contentScale = contentScale,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ArtworkFailed(
    mediaId: String,
    cropType: ImageCrop,
    isDetail: Boolean,
    appString: String
) {
    val artworkImageApple = ArtworkImageApple(isDetail)
    val state = artworkImageApple.artworkUrl.observeAsState()
    artworkImageApple.getSongData(mediaId)

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val indicator = createRef()
        CustomGlide(
            imageModel = state.value,
            cropType = cropType,
            isDetail = isDetail,
            modifier = Modifier
                .constrainAs(indicator) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

fun getArtworkPath(url: String?, artworkId: String?, isDetail: Boolean, context: Context): String {
    url?.let {
        Log.d("artwork", url.toString())
        return if (isDetail) {
            url.plus("${DETAIL_IMAGE_SIZE}x${DETAIL_IMAGE_SIZE}.jpg")
        } else {
            url.plus("$IMAGE_SIZE}x${IMAGE_SIZE}.jpg")
        }
    }
    return generateLocalArtworkPath(artworkId, context)
}

fun generateLocalArtworkPath(artworkId: String?, context: Context): String {
    artworkId?.let {
        return Paths.get(
            context.filesDir.absolutePath,
            "$artworkId.jpeg"
        ).toString()
    }
    return ""
}

class ArtworkImageApple(private val isDetail: Boolean = false) {

    val artworkUrl = MutableLiveData<String>().apply {
        value = ""
    }

    fun getSongData(mediaId: String) = CoroutineScope(Dispatchers.Default).launch {
        val apiResult = try {
            ApiProvider.appleApi.getSongInfo(mediaId).execute()
        } catch (e: Exception) {
            Log.d("api", e.toString())
            null
        }
        apiResult?.let {
            Log.d("api", "success")
            it.body()?.appleSearchResults?.let { list ->
                if (list.isNotEmpty()) {
                    list[0].artworkUrl100?.let { artworkUrl100 ->
                        artworkUrl.postValue(generateArtworkUrl(artworkUrl100))
                    }
                }
            }
        }
    }

    private fun generateArtworkUrl(baseUrl: String): String {
        val imagePath = Path(baseUrl)
        return if (isDetail) {
            baseUrl.replace(imagePath.fileName.toString(), "${DETAIL_IMAGE_SIZE}x${DETAIL_IMAGE_SIZE}.jpg")
        } else {
            baseUrl.replace(imagePath.fileName.toString(), "${IMAGE_SIZE}x${IMAGE_SIZE}.jpg")
        }
    }
}