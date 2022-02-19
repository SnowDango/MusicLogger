package com.snowdango.musiclogger.view.model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.IMAGE_SIZE
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.glide.customRequestBuilder
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.common.glide.ImageCrop
import java.nio.file.Paths


class ArtworkView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val artwork: ImageView
    private val progress: ProgressBar
    private val requestManager: RequestManager
    private val helper: ArtworkViewHelper

    init {
        LayoutInflater.from(context).inflate(R.layout.artwork_view, this, true)
        artwork = findViewById(R.id.artworkImageView)
        progress = findViewById(R.id.artworkProgress)
        requestManager = Glide.with(context)
        helper = ArtworkViewHelper()
    }

    @SuppressLint("CheckResult")
    fun update(musicMetaWithArt: MusicMetaWithArt, cropType: ImageCrop) {
        start()
        val artworkPath = getArtworkPath(musicMetaWithArt.url, musicMetaWithArt.artworkId, true)
        if (artworkPath.isBlank()) {
            fetchApiArtwork(musicMetaWithArt, cropType)
        } else {
            loadArtwork(artworkPath, cropType)
        }
    }

    fun clear() {
        requestManager.clear(artwork)
    }

    private fun start() {
        artwork.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE
    }

    private fun finish() {
        artwork.visibility = View.VISIBLE
        progress.visibility = View.INVISIBLE
    }

    private fun fetchApiArtwork(musicMetaWithArt: MusicMetaWithArt, cropType: ImageCrop) {
        findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
            helper.artworkPath.observe(lifecycleOwner) {
                it?.let { fetchArtworkPath ->
                    loadArtwork(fetchArtworkPath, cropType)
                }
            }
            helper.fetchArtwork(musicMetaWithArt)
        }
    }

    private fun loadArtwork(artworkPath: String, cropType: ImageCrop) {
        requestManager.customRequestBuilder(cropType).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                finish()
                artwork.setImageResource(R.drawable.failed_image)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                finish()
                return false
            }
        }).load(artworkPath).into(artwork)
    }

    private fun getArtworkPath(url: String?, artworkId: String?, isDetail: Boolean): String {
        url?.let {
            return if (isDetail) {
                url.plus("${DETAIL_IMAGE_SIZE}x$DETAIL_IMAGE_SIZE.jpg")
            } else {
                url.plus("$IMAGE_SIZE}x$IMAGE_SIZE.jpg")
            }
        }
        return generateLocalArtworkPath(artworkId)
    }

    private fun generateLocalArtworkPath(artworkId: String?): String {
        artworkId?.let {
            return Paths.get(
                context.filesDir.absolutePath, "$artworkId.jpeg"
            ).toString()
        }
        return ""
    }
}