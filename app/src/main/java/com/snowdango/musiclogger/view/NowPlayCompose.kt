package com.snowdango.musiclogger.view

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.connection.ViewObject
import com.snowdango.musiclogger.domain.session.MusicMeta
import org.koin.androidx.compose.get

@Preview
@Composable
fun NowPlayCompose(context: Context = get()){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
        constraintSet = createConstrainsSet()
    ) {
        val musicMetaState = ViewObject.musicMeta.observeAsState(
            initial = MusicMeta(
                title = "No Song",
                artist = "No Artist",
                album = "No Album",
                albumArtist = null,
                app = "",
                mediaId = null,
                artwork = context.getDrawable(R.drawable.test)!!.toBitmap()
            )
        )
        Text(
            text = "Last Song",
            modifier = Modifier.layoutId("lastSongText"),
            fontSize = 20.sp,
            color = Color.White
        )
        val displayMetrics = context.resources.displayMetrics
        val widthMaxDp = displayMetrics.widthPixels / displayMetrics.density
        GlideImage(
            imageModel = musicMetaState.value.artwork,
            requestOptions = {
                RequestOptions()
                    .override(1024, 1024)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
            },
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = "Last Playing Artwork",
            modifier = Modifier
                .layoutId("lastPlayingArtwork")
                .height((widthMaxDp / 2).dp)
                .width((widthMaxDp / 2).dp),
        )
        Text(
            text = musicMetaState.value.title,
            modifier = Modifier.layoutId("songTitle"),
            fontSize = 20.sp,
            color = Color.White
        )
        Text(
            text = musicMetaState.value.album?: "",
            modifier = Modifier.layoutId("albumName"),
            fontSize = 15.sp,
            color = Color.LightGray
        )
        Text(
            text = musicMetaState.value.artist?: "",
            modifier = Modifier.layoutId("artistName"),
            fontSize = 15.sp,
            color = Color.LightGray
        )
    }
}

private fun createConstrainsSet(): ConstraintSet {
    return ConstraintSet {
        val lastSongText = createRefFor("lastSongText")
        val nowPlayingArtwork = createRefFor("lastPlayingArtwork")
        val songTitle = createRefFor("songTitle")
        val albumName = createRefFor("albumName")
        val artistName = createRefFor("artistName")

        constrain(lastSongText){
            centerHorizontallyTo(parent)
            top.linkTo(parent.top, 35.dp)
            bottom.linkTo(nowPlayingArtwork.top, 5.dp)
        }
        constrain(nowPlayingArtwork){
            centerHorizontallyTo(parent)
            top.linkTo(lastSongText.bottom, 5.dp)
            bottom.linkTo(songTitle.top)
        }
        constrain(songTitle){
            centerHorizontallyTo(parent)
            top.linkTo(nowPlayingArtwork.bottom, 5.dp)
            bottom.linkTo(albumName.top)
        }
        constrain(albumName){
            centerHorizontallyTo(parent)
            top.linkTo(songTitle.bottom, 5.dp)
            bottom.linkTo(artistName.top)
        }
        constrain(artistName){
            centerHorizontallyTo(parent)
            top.linkTo(albumName.bottom,5.dp)
        }
    }
}