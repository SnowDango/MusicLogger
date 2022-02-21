package com.snowdango.musiclogger.view.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.glide.ImageCrop
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import com.snowdango.musiclogger.view.model.AppIconView
import com.snowdango.musiclogger.view.model.ArtworkView


@ModelView(defaultLayout = R.layout.grid_item_album)
class AlbumView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var artwork: ArtworkView
    private lateinit var appIcon: AppIconView
    private lateinit var album: TextView
    private lateinit var artist: TextView

    private var albumWithArt: AlbumWithArt? = null
    private var artworkSize: Int? = null
    private var clickListener: ((View) -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        artwork = findViewById(R.id.albumArtworkView)
        appIcon = findViewById(R.id.albumAppIcon)
        album = findViewById(R.id.albumName)
        artist = findViewById(R.id.albumArtist)
        update()
    }


    @ModelProp
    fun setAlbumWithArt(albumWithArt: AlbumWithArt) {
        this.albumWithArt = albumWithArt
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
        albumWithArt?.let {
            // artwork
            artworkSize?.let { artSize ->
                artwork.layoutParams.width = artSize
                artwork.layoutParams.height = artSize
            }
            val artworkViewData = ArtworkView.ArtworkViewData(
                it.url, it.artworkId, it.mediaId, it.appString!!
            )
            artwork.update(artworkViewData, ImageCrop.RoundedCorner)

            // appIcon
            appIcon.update(it.appString)

            // album
            album.text = it.album ?: ""

            // artist
            artist.text = it.albumArtist ?: ""
        }
        clickListener?.let { func ->
            setOnClickListener { func(it) }
        }
    }
}