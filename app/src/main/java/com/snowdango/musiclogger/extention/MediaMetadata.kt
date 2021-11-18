package com.snowdango.musiclogger.extention

import android.graphics.Bitmap
import android.media.MediaMetadata

fun MediaMetadata.title(): String = this.getString(MediaMetadata.METADATA_KEY_TITLE)

fun MediaMetadata.artwork(): Bitmap = this.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)

fun MediaMetadata.artist(): String = this.getString(MediaMetadata.METADATA_KEY_ARTIST)

fun MediaMetadata.album(): String = this.getString(MediaMetadata.METADATA_KEY_ALBUM)

fun MediaMetadata.mediaId(): String = this.getString(MediaMetadata.METADATA_KEY_MEDIA_ID)
