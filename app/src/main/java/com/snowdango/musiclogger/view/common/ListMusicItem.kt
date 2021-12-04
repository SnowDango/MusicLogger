package com.snowdango.musiclogger.view.common

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.extention.fromUnix2String
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import org.koin.androidx.compose.get
import java.nio.file.Paths

private const val ARTWORK_ID = "history-artwork"
private const val TITLE_ID = "history-title"
private const val ALBUM_ID = "history-album"
private const val ARTIST_ID = "history-artist"
private const val DATE_ID = "history-date"

@Preview
@Composable
fun ListMusicItem(musicMetaWithArt: MusicMetaWithArt, context: Context = get()) {
    val displayMetrics = context.resources.displayMetrics
    val widthMaxDp = displayMetrics.widthPixels / displayMetrics.density
    val artworkSize = (widthMaxDp / 6)
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth(),
        constraintSet = createConstrainsSet(widthMaxDp)
    ) {
        GlideImage(
            imageModel = Paths.get(
                context.filesDir.absolutePath,
                "${musicMetaWithArt.artworkId}.jpeg"
            ).toString(),
            requestOptions = {
                RequestOptions()
                    .override(1024, 1024)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .error(context.getDrawable(R.drawable.test))
            },
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = "Last Playing Artwork",
            modifier = Modifier
                .layoutId(ARTWORK_ID)
                .height(artworkSize.dp)
                .width(artworkSize.dp),
        )
        MarqueeText(
            text = musicMetaWithArt.title,
            modifier = Modifier
                .layoutId(TITLE_ID),
            fontSize = 15.sp,
            color = Color.White
        )
        MarqueeText(
            text = musicMetaWithArt.album ?: "",
            modifier = Modifier
                .layoutId(ALBUM_ID),
            fontSize = 10.sp,
            color = Color.LightGray
        )
        MarqueeText(
            text = musicMetaWithArt.artist ?: musicMetaWithArt.albumArtist ?: "",
            modifier = Modifier
                .layoutId(ARTIST_ID),
            fontSize = 10.sp,
            color = Color.LightGray
        )
        Text(
            text = musicMetaWithArt.listeningUnix.fromUnix2String(),
            modifier = Modifier.layoutId(DATE_ID),
            fontSize = 8.sp,
            color = Color.LightGray
        )
        /*GlideImage(
            imageModel = context.getDrawable(R.drawable.apple_music),
            requestOptions = {
                RequestOptions()
                    .override(512, 512)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .error(context.getDrawable(R.drawable.test))
            },
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = "app icon",
            modifier = Modifier
                .layoutId(APP_ICON_ID)
                .height(appIconSize.dp)
                .width(appIconSize.dp),
        )*/
    }
}

private fun createConstrainsSet(widthMaxDp: Float): ConstraintSet {
    val startMargin = 10
    val betweenMargin = 5
    val endMargin = 15
    val totalMargin = startMargin + betweenMargin + endMargin
    val textSize = ((widthMaxDp * 5 / 6) - totalMargin).dp
    return ConstraintSet {
        val artwork = createRefFor(ARTWORK_ID)
        val title = createRefFor(TITLE_ID)
        val album = createRefFor(ALBUM_ID)
        val artist = createRefFor(ARTIST_ID)
        val date = createRefFor(DATE_ID)
        //val appIcon = createRefFor(APP_ICON_ID)

        constrain(artwork) {
            top.linkTo(parent.top, 3.dp)
            start.linkTo(parent.start, startMargin.dp)
            end.linkTo(title.start)
        }
        constrain(title) {
            width = Dimension.value(textSize)
            height = Dimension.wrapContent
            top.linkTo(artwork.top)
            start.linkTo(artwork.end, betweenMargin.dp)
            bottom.linkTo(album.top)
        }
        constrain(album) {
            width = Dimension.value(textSize)
            height = Dimension.wrapContent
            top.linkTo(title.bottom)
            start.linkTo(artwork.end, betweenMargin.dp)
            bottom.linkTo(artist.top)
        }
        constrain(artist) {
            width = Dimension.value(textSize)
            height = Dimension.wrapContent
            top.linkTo(album.bottom)
            start.linkTo(artwork.end, betweenMargin.dp)
            bottom.linkTo(date.bottom)
        }
        constrain(date) {
            width = Dimension.preferredWrapContent
            height = Dimension.preferredWrapContent
            top.linkTo(artist.bottom)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end, endMargin.dp)
        }
        /*constrain(appIcon) {
            bottom.linkTo(date.top)
            end.linkTo(parent.end, endMargin.dp)
        }*/
    }
}