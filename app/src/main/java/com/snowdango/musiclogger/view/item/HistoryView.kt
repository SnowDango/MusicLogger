package com.snowdango.musiclogger.view.item

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.bumptech.glide.Glide
import com.snowdango.musiclogger.App
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.extention.fromUnix2String
import com.snowdango.musiclogger.glide.customRequestBuilder
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.common.glide.ImageCrop
import com.snowdango.musiclogger.view.model.ArtworkView


@ModelView(defaultLayout = R.layout.list_item_history)
class HistoryView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val appIconRequestManager by lazy { Glide.with(context) }

    private lateinit var artwork: ArtworkView
    private lateinit var title: TextView
    private lateinit var album: TextView
    private lateinit var artist: TextView
    private lateinit var date: TextView
    private lateinit var appIcon: ImageView

    private var musicMetaWithArt: MusicMetaWithArt? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        artwork = findViewById(R.id.artworkView)
        title = findViewById(R.id.historyTitle)
        album = findViewById(R.id.historyAlbum)
        artist = findViewById(R.id.historyArtist)
        date = findViewById(R.id.historyDate)
        appIcon = findViewById(R.id.historyAppIcon)
        update()
    }


    @ModelProp
    fun setMusicMetaWithArt(musicMetaWithArt: MusicMetaWithArt) {
        this.musicMetaWithArt = musicMetaWithArt
    }

    @OnViewRecycled
    fun clear() {
        appIconRequestManager.clear(appIcon)
        artwork.clear()
    }

    @AfterPropsSet
    fun update() {
        musicMetaWithArt?.let {
            // artwork
            val artworkSize = ((App.deviceMaxWidth / 6) * App.density).toInt()
            artwork.layoutParams.also { layoutParams ->
                layoutParams.width = artworkSize
                layoutParams.height = artworkSize
            }
            artwork.update(it, ImageCrop.Circle)

            // title
            title.text = it.title
            // album
            album.text = it.album ?: ""
            // artist
            artist.text = it.artist ?: (it.albumArtist ?: "")
            // date
            date.text = it.listeningUnix.fromUnix2String()

            //appIcon
            appIconRequestManager.customRequestBuilder(ImageCrop.Circle)
                .load("file:///android_asset/${it.appString}.png").into(appIcon)
        }
    }
}