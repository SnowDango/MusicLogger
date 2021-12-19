package com.snowdango.musiclogger.view.common

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import org.koin.androidx.compose.get
import java.nio.file.Paths

private const val ARTWORK_ID = "album-artwork"
private const val ALBUM_ID = "album-name"
private const val ARTIST_ID = "album-artist"
private const val APP_ICON_ID = "album-app"

@Composable
fun GridAlbumItem(albumWithArt: AlbumWithArt, context: Context = get(), spanCount: Int) {
    val displayMetrics = context.resources.displayMetrics
    val widthMaxDp = displayMetrics.widthPixels / displayMetrics.density
    val totalSpanBetween = spanCount + 1
    val verticalPadding = 5
    val horizontalPadding = 5
    val verticalTotalPadding = totalSpanBetween * (verticalPadding * 2)
    val cardVerticalSize = (widthMaxDp - verticalTotalPadding) / 2
    val artworkSize = cardVerticalSize * 2 / 3
    val appIconSize = 15
    Card(
        modifier = Modifier
            .padding(horizontalPadding.dp, verticalPadding.dp, (horizontalPadding / 2).dp, verticalPadding.dp)
            .height(cardVerticalSize.dp)
            .wrapContentWidth(),
        shape = RoundedCornerShape(10),
        backgroundColor = colorResource(R.color.backGround)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(verticalPadding.dp),
            constraintSet = createConstraintSet(),
        ) {
            CustomGlide(
                imageModel = "file:///android_asset/${albumWithArt.appString}.png",
                contentDescription = "app icon",
                modifier = Modifier
                    .layoutId(APP_ICON_ID)
                    .height(appIconSize.dp)
                    .width(appIconSize.dp)
            )
            CustomGlide(
                imageModel = Paths.get(
                    context.filesDir.absolutePath,
                    "${albumWithArt.artworkId}.jpeg"
                ).toString(),
                cropType = ImageCrop.RoundedCorner,
                contentDescription = "Last Playing Artwork",
                modifier = Modifier
                    .layoutId(ARTWORK_ID)
                    .height(artworkSize.dp)
                    .width(artworkSize.dp)
            )
            MarqueeText(
                text = albumWithArt.album ?: "",
                modifier = Modifier
                    .layoutId(ALBUM_ID)
                    .wrapContentHeight()
                    .wrapContentWidth(),
                fontSize = 15.sp,
                color = colorResource(R.color.textColor),
                textAlign = TextAlign.Center
            )
            MarqueeText(
                text = albumWithArt.albumArtist ?: "",
                modifier = Modifier
                    .layoutId(ARTIST_ID)
                    .wrapContentWidth()
                    .wrapContentHeight(),
                fontSize = 15.sp,
                color = colorResource(R.color.textColor),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun createConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val appIcon = createRefFor(APP_ICON_ID)
        val artwork = createRefFor(ARTWORK_ID)
        val album = createRefFor(ALBUM_ID)
        val artist = createRefFor(ARTIST_ID)

        constrain(artwork) {
            top.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(album.top)
        }
        constrain(appIcon) {
            bottom.linkTo(artwork.bottom)
            start.linkTo(artwork.end)
            end.linkTo(parent.end)
        }
        constrain(album) {
            top.linkTo(artwork.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(artist.top)
        }
        constrain(artist) {
            top.linkTo(album.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
    }
}