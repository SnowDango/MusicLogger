package com.snowdango.musiclogger.view.common.glide

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.snowdango.musiclogger.DETAIL_IMAGE_SIZE
import com.snowdango.musiclogger.IMAGE_SIZE
import com.snowdango.musiclogger.R
import org.koin.androidx.compose.get

@Composable
fun CustomGlide(
    imageModel: Any?,
    cropType: ImageCrop = ImageCrop.Circle,
    isDetail: Boolean = false,
    loading: @Composable BoxScope.(GlideImageState.Loading) -> Unit = { GlideLoading() },
    failed: @Composable BoxScope.(GlideImageState.Failure) -> Unit = { GlideFailed() },
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = "",
    modifier: Modifier = Modifier,
) {
    GlideImage(
        imageModel = imageModel,
        loading = loading,
        failure = failed,
        requestOptions = { customGlideRequestOption(cropType, isDetail) },
        requestBuilder = { customGlideRequestBuilder() },
        alignment = alignment,
        contentScale = contentScale,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Composable
fun GlideLoading() {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val indicator = createRef()
        CircularProgressIndicator(
            modifier = Modifier.constrainAs(indicator) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun GlideFailed(context: Context = get()) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val indicator = createRef()
        GlideImage(
            imageModel = context.getDrawable(R.drawable.failed_image),
            requestBuilder = { customGlideRequestBuilder() },
            modifier = Modifier
                .constrainAs(indicator) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@SuppressLint("CheckResult")
fun customGlideRequestOption(cropType: ImageCrop, isDetail: Boolean): RequestOptions {
    val requestOptions = RequestOptions()
    requestOptions.let {
        if (isDetail) {
            it.override(DETAIL_IMAGE_SIZE, DETAIL_IMAGE_SIZE)
        } else {
            it.override(IMAGE_SIZE, IMAGE_SIZE)
        }
        it.diskCacheStrategy(DiskCacheStrategy.ALL)
        if (cropType == ImageCrop.Circle) {
            it.circleCrop()
        }
        if (cropType == ImageCrop.RoundedCorner) {
            val transformation = MultiTransformation(CenterCrop(), RoundedCorners(80))
            it.transform(transformation)
        }
    }
    return requestOptions
}

@Composable
fun customGlideRequestBuilder(): RequestBuilder<Drawable> {
    return Glide.with(LocalContext.current.applicationContext).asDrawable()
}

enum class ImageCrop {
    Circle,
    RoundedCorner
}
