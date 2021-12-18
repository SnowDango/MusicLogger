package com.snowdango.musiclogger.view.common

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.snowdango.musiclogger.IMAGE_SIZE
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import org.koin.androidx.compose.get
import java.nio.file.Paths

private const val ARTWORK_ID = "album-artwork"
private const val ALBUM_ID = "album-name"
private const val ARTIST_ID = "album-artist"

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
    Card(
        modifier = Modifier
            .padding(horizontalPadding.dp, verticalPadding.dp, (horizontalPadding / 2).dp, verticalPadding.dp)
            .height(cardVerticalSize.dp)
            .wrapContentWidth(),
        elevation = 20.dp,
        backgroundColor = colorResource(R.color.cardBackGround)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(verticalPadding.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val transformation = MultiTransformation(CenterCrop(), RoundedCorners(80))
            GlideImage(
                imageModel = Paths.get(
                    context.filesDir.absolutePath,
                    "${albumWithArt.artworkId}.jpeg"
                ).toString(),
                requestOptions = {
                    RequestOptions()
                        .override(IMAGE_SIZE, IMAGE_SIZE)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transform(transformation)
                        .placeholder(context.getDrawable(R.drawable.place_holder))
                },
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                contentDescription = "Last Playing Artwork",
                modifier = Modifier
                    .layoutId(ARTWORK_ID)
                    .height(artworkSize.dp)
                    .width(artworkSize.dp)
                    .background(Color.Gray, RoundedCornerShape(20.dp)),
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