package com.snowdango.musiclogger.view.model

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.bumptech.glide.Glide
import com.snowdango.musiclogger.App
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.IMAGE_SIZE
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.extention.fromUnix2String
import com.snowdango.musiclogger.glide.customRequestBuilder
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.common.glide.ImageCrop
import java.nio.file.Paths


@ModelView(defaultLayout = R.layout.list_item_history)
class HistoryView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val artworkRequestManager by lazy { Glide.with(context) }
    private val appIconRequestManager by lazy { Glide.with(context) }

    private lateinit var artwork: ImageView
    private lateinit var title: TextView
    private lateinit var album: TextView
    private lateinit var artist: TextView
    private lateinit var date: TextView
    private lateinit var appIcon: ImageView

    private var musicMetaWithArt: MusicMetaWithArt? = null
    private var deviceMaxWidth: Int = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        artwork = findViewById(R.id.historyArtwork)
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

    @ModelProp
    fun setDeviceMaxWidth(deviceMaxWidth: Int) {
        this.deviceMaxWidth = deviceMaxWidth
    }

    @OnViewRecycled
    fun clear() {
        artworkRequestManager.clear(artwork)
        appIconRequestManager.clear(appIcon)
    }

    @AfterPropsSet
    fun update() {
        musicMetaWithArt?.let {
            // artwork
            val artworkSize = ((deviceMaxWidth / 6) * App.density).toInt()
            artwork.layoutParams.also { layoutParams ->
                layoutParams.width = artworkSize
                layoutParams.height = artworkSize
            }
            val artworkPath = getArtworkPath(it.url, it.artworkId, true)
            artworkRequestManager.customRequestBuilder(ImageCrop.Circle, true).load(artworkPath).into(artwork)

            // title
            title.text = it.title
            // album
            album.text = it.album ?: ""
            // artist
            artist.text = it.artist ?: (it.albumArtist ?: "")
            // date
            date.text = it.listeningUnix.fromUnix2String()

            //appIcon
            appIconRequestManager.customRequestBuilder(ImageCrop.Circle, false)
                .load("file:///android_asset/${it.appString}.png").into(appIcon)
        }
    }

    private fun getArtworkPath(url: String?, artworkId: String?, isDetail: Boolean): String {
        url?.let {
            Log.d("artwork", url.toString())
            return if (isDetail) {
                url.plus("${DETAIL_IMAGE_SIZE}x${DETAIL_IMAGE_SIZE}.jpg")
            } else {
                url.plus("$IMAGE_SIZE}x${IMAGE_SIZE}.jpg")
            }
        }
        return generateLocalArtworkPath(artworkId)
    }

    private fun generateLocalArtworkPath(artworkId: String?): String {
        artworkId?.let {
            return Paths.get(
                context.filesDir.absolutePath,
                "$artworkId.jpeg"
            ).toString()
        }
        return ""
    }
}