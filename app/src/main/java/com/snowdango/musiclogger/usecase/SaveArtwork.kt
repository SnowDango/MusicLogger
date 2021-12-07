package com.snowdango.musiclogger.usecase

import android.content.Context
import android.os.storage.StorageManager
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.*
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.ArtworkData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class SaveArtwork(private val musicDataBase: MusicDataBase, private val context: Context) {

    suspend fun saveArtwork(musicMeta: MusicMeta) = withContext(Dispatchers.IO){
        if (!isAlreadySave(musicMeta.albumArtist, musicMeta.album)) { // not save artwork
            if (hasNeedInfo(musicMeta)) {
                val imageUuid = UUID.randomUUID().toString()
                requestStorage(musicMeta.artwork!!.allocationByteCount.toLong(), imageUuid)?.let { file ->
                    if (isSuccessImageSave(musicMeta.artwork.toJpegByteArray(), file))
                        registerImage(imageUuid, musicMeta.album!!, musicMeta.albumArtist!!)
                }
            }
        }
    }

    private fun hasNeedInfo(meta: MusicMeta): Boolean { // has need information?
        return meta.albumArtist != null && meta.album != null
    }

    private fun isAlreadySave(albumArtist: String?, album: String?): Boolean { // is image already save?
        return musicDataBase.artworkDao().getArtworkId(album, albumArtist) != null
    }

    private fun requestStorage(imageSize: Long, imageUuid: String): File? { // is storage enough?
        val file = File(context.filesDir, "$imageUuid.jpeg")
        val storageManager: StorageManager =
            context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageUuid = storageManager.getUuidForPath(file)
        val freeStorage = storageManager.getAllocatableBytes(storageUuid)
        return if (imageSize < freeStorage) {
            file
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "アートワークに必要な容量が足りません。", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun registerImage(imageUuid: String, album: String, albumArtist: String) { // save image uuid
        try {
            musicDataBase.artworkDao().insert(
                ArtworkData(
                    id = 0,
                    imageId = imageUuid,
                    album = album,
                    artist = albumArtist
                )
            )
        }catch (e: Exception){
            Log.d("db_failed", musicDataBase.artworkDao().getArtworkData(album = album, albumArtist = albumArtist).toString())
            Log.d("db_failed", musicDataBase.artworkDao().getArtworkDataLimit100(0).toString())
        }
    }

    private fun isSuccessImageSave(image: ByteArray, file: File): Boolean { // save image and it result
        return try {
            val fileOutputStream = file.outputStream()
            fileOutputStream.write(image)
            fileOutputStream.close()
            true
        } catch (e: Exception) {
            false
        }
    }

}