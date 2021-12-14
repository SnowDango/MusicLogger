package com.snowdango.musiclogger.view.common

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import org.koin.androidx.compose.get
import java.nio.file.Paths

private const val ARTWORK_ID = "history-artwork"

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
            .wrapContentHeight()
            .width(cardVerticalSize.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(verticalPadding.dp),
            constraintSet = createAlbumConstrainsSet()
        ) {
            GlideImage(
                imageModel = Paths.get(
                    context.filesDir.absolutePath,
                    "${albumWithArt.artworkId}.jpeg"
                ).toString(),
                requestOptions = {
                    RequestOptions()
                        .override(1024, 1024)
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
                    .background(Color.Gray, RectangleShape),
            )
        }
    }
}

fun createAlbumConstrainsSet(): ConstraintSet {
    return ConstraintSet {

    }
}