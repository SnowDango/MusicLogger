package com.snowdango.musiclogger.view.common

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.snowdango.musiclogger.IMAGE_SIZE
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
private const val APP_ICON_ID = "history-app"

@Composable
fun ListMusicItem(musicMetaWithArt: MusicMetaWithArt, context: Context = get()) {
    val displayMetrics = context.resources.displayMetrics
    val widthMaxDp = displayMetrics.widthPixels / displayMetrics.density
    val artworkSize = (widthMaxDp / 6)
    val verticalPadding = 10
    val horizontalPadding = 5
    val appIconSize = 15
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth().padding(verticalPadding.dp, horizontalPadding.dp),
        constraintSet = createMusicConstrainsSet(widthMaxDp, verticalPadding, appIconSize),
    ) {
        GlideImage(
            imageModel = Paths.get(
                context.filesDir.absolutePath,
                "${musicMetaWithArt.artworkId}.jpeg"
            ).toString(),
            loading = {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val indicator = createRef()
                    CircularProgressIndicator(
                        modifier = Modifier.constrainAs(indicator) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }
            },
            failure = {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val indicator = createRef()
                    GlideImage(
                        imageModel = context.getDrawable(R.drawable.place_holder),
                        modifier = Modifier
                            .background(colorResource(R.color.cardBackGround), CircleShape)
                            .constrainAs(indicator) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }
            },
            requestOptions = {
                RequestOptions()
                    .override(IMAGE_SIZE, IMAGE_SIZE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .placeholder(context.getDrawable(R.drawable.place_holder))
            },
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = "Last Playing Artwork",
            modifier = Modifier
                .layoutId(ARTWORK_ID)
                .height(artworkSize.dp)
                .width(artworkSize.dp)
                .background(Color.Gray, CircleShape),
        )
        MarqueeText(
            text = musicMetaWithArt.title,
            modifier = Modifier
                .layoutId(TITLE_ID),
            fontSize = 15.sp,
            color = colorResource(R.color.textColor)
        )
        MarqueeText(
            text = musicMetaWithArt.album ?: "",
            modifier = Modifier
                .layoutId(ALBUM_ID),
            fontSize = 10.sp,
            color = colorResource(R.color.describeText)
        )
        MarqueeText(
            text = musicMetaWithArt.artist ?: (musicMetaWithArt.albumArtist ?: ""),
            modifier = Modifier
                .layoutId(ARTIST_ID),
            fontSize = 10.sp,
            color = colorResource(R.color.describeText)
        )
        Text(
            text = musicMetaWithArt.listeningUnix.fromUnix2String(),
            modifier = Modifier.layoutId(DATE_ID),
            fontSize = 8.sp,
            color = colorResource(R.color.describeText)
        )
        GlideImage(
            imageModel = "file:///android_asset/${musicMetaWithArt.appString}.png",
            requestOptions = {
                RequestOptions()
                    .override(IMAGE_SIZE, IMAGE_SIZE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
            },
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = "app icon",
            modifier = Modifier
                .layoutId(APP_ICON_ID)
                .height(appIconSize.dp)
                .width(appIconSize.dp),
        )
    }
}

private fun createMusicConstrainsSet(widthMaxDp: Float, verticalPadding: Int, appIconSize: Int): ConstraintSet {
    val betweenMargin = 5
    val totalMargin = 2 * (betweenMargin + verticalPadding)
    val textSize = ((widthMaxDp * 5 / 6) - totalMargin - appIconSize).dp
    return ConstraintSet {
        val artwork = createRefFor(ARTWORK_ID)
        val title = createRefFor(TITLE_ID)
        val album = createRefFor(ALBUM_ID)
        val artist = createRefFor(ARTIST_ID)
        val date = createRefFor(DATE_ID)
        val appIcon = createRefFor(APP_ICON_ID)

        constrain(artwork) {
            top.linkTo(parent.top, 3.dp)
            start.linkTo(parent.start)
            end.linkTo(title.start)
            bottom.linkTo(parent.bottom, 3.dp)
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
            end.linkTo(parent.end)
        }
        constrain(appIcon) {
            top.linkTo(album.top)
            bottom.linkTo(date.top)
            end.linkTo(parent.end)
        }
    }
}