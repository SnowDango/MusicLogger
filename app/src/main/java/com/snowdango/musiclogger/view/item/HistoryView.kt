package com.snowdango.musiclogger.view.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.extention.fromUnix2String
import com.snowdango.musiclogger.glide.ImageCrop
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.model.AppIconView
import com.snowdango.musiclogger.view.model.ArtworkView
import timber.log.Timber


@ModelView(defaultLayout = R.layout.list_item_history)
class HistoryView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var artwork: ArtworkView
    private lateinit var title: TextView
    private lateinit var album: TextView
    private lateinit var artist: TextView
    private lateinit var date: TextView
    private lateinit var appIcon: AppIconView

    private var musicMetaWithArt: MusicMetaWithArt? = null
    private var artworkSize: Int? = null
    private var clickListener: ((View) -> Unit)? = null

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

    @ModelProp
    fun setArtworkSize(artworkSize: Int) {
        this.artworkSize = artworkSize
    }

    @ModelProp(options = [ModelProp.Option.IgnoreRequireHashCode])
    fun setClickListener(clickListener: ((View) -> Unit)?) {
        this.clickListener = clickListener
    }

    @OnViewRecycled
    fun clear() {
        artwork.clear()
        appIcon.clear()
    }

    @AfterPropsSet
    fun update() {
        musicMetaWithArt?.let {
            // artwork
            artworkSize?.let { artSize ->
                artwork.layoutParams.width = artSize
                artwork.layoutParams.height = artSize
            }
            val artworkViewData = ArtworkView.ArtworkViewData(
                it.url, it.artworkId, it.mediaId, it.appString
            )
            Timber.d("${it.title}: ${it.artworkId}")
            artwork.update(artworkViewData, ImageCrop.Circle)

            // title
            title.text = it.title
            title.isSelected = true
            // album
            album.text = it.album ?: ""
            album.isSelected = true
            // artist
            artist.text = it.artist ?: (it.albumArtist ?: "")
            artist.isSelected = true
            // date
            date.text = it.listeningUnix.fromUnix2String()

            //appIcon
            appIcon.update(it.appString)
        }
        clickListener?.let { func ->
            setOnClickListener { func(it) }
        }
    }
}