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
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.IMAGE_SIZE
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.glide.CustomGlide
import com.snowdango.musiclogger.glide.ImageCrop
import timber.log.Timber
import java.nio.file.Paths


class ArtworkView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val artwork: ImageView
    private val progress: ProgressBar
    private val requestManager: RequestManager

    init {
        LayoutInflater.from(context).inflate(R.layout.artwork_view, this, true)
        artwork = findViewById(R.id.artworkImageView)
        progress = findViewById(R.id.artworkProgress)
        requestManager = CustomGlide.with(context)
    }

    @SuppressLint("CheckResult")
    fun update(artworkViewData: ArtworkViewData, cropType: ImageCrop) {
        start()
        val artworkPath = getArtworkPath(artworkViewData.url, artworkViewData.artworkId, true)
        Timber.d("${artworkPath}")
        if (artworkPath.isBlank()) {
            apiFetchArtwork(artworkViewData, cropType)
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

    fun loadArtwork(artworkPath: String?, cropType: ImageCrop) {
        val requestBuilder =
            requestBuilder(requestManager, cropType, { _: GlideException?, _: Any?, _: Target<Drawable>?, _: Boolean ->
                Timber.e("failed image load")
                finish()
                artwork.setImageResource(R.drawable.failed_image)
            }, { _: Drawable?, _: Any?, _: Target<Drawable>?, _: DataSource?, _: Boolean ->
                finish()
            })
        if (artworkPath != null) {
            requestBuilder.load(artworkPath).into(artwork)
        } else {
            requestBuilder.load(R.drawable.failed_image).into(artwork)
        }
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

    data class ArtworkViewData(
        val url: String?, val artworkId: String?, val mediaId: String?, val appString: String
    )

}