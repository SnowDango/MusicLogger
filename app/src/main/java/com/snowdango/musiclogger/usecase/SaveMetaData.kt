package com.snowdango.musiclogger.usecase

import android.content.Context
import android.os.storage.StorageManager
import android.util.Log
import android.widget.Toast
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.toJpegByteArray
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.ArtworkData
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.soywiz.klock.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

object SaveMetaData {

    fun saveMusic(musicDataBase: MusicDataBase, musicMeta: MusicMeta, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        saveArtwork(musicDataBase, musicMeta, context)
        musicDataBase.musicMetaDao().insert(
            MusicMetadata(
                title = musicMeta.title,
                album = musicMeta.album,
                artist = musicMeta.artist,
                albumArtist = musicMeta.albumArtist,
                listeningUnix = DateTime.nowUnixLong(),
                mediaId = musicMeta.mediaId,
                app = musicMeta.app
            ))
        Log.d("history with data", musicDataBase.musicMetaWithArtDao().get().toString())

    }

    fun saveArtwork(musicDataBase: MusicDataBase, musicMeta: MusicMeta, context: Context){
        if(!isAlreadySave(musicDataBase,musicMeta.albumArtist,musicMeta.album)){ // not save artwork
            if(hasNeedInfo(musicMeta)){
                val imageUuid = UUID.randomUUID().toString()
                requestStorage(
                    context,
                    musicMeta.artwork!!.allocationByteCount.toLong(),
                    imageUuid
                )?.let { file ->
                    if(isSuccessImageSave(musicMeta.artwork.toJpegByteArray(),file)){
                        registerImage(musicDataBase, imageUuid, musicMeta.album!!, musicMeta.albumArtist!!)
                    }
                }
            }else{
                Log.d("need　info","必要なデータがありません。")
            }
        }else{
            Log.d("already image ", "save済")
        }
    }

    private fun hasNeedInfo(meta: MusicMeta): Boolean{ // has need information?
        return meta.albumArtist != null && meta.album != null && meta.artwork != null
    }

    private fun isAlreadySave(dataBase: MusicDataBase, albumArtist: String?, album: String?): Boolean{ // is image already save?
        return dataBase.artworkDao().getArtworkId(album,albumArtist) != null
    }

    private fun requestStorage(context: Context, imageSize: Long, imageUuid: String): File?{ // is storage enough?
        val file = File(context.filesDir, "$imageUuid.jpeg")
        val storageManager: StorageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageUuid = storageManager.getUuidForPath(file)
        val freeStorage = storageManager.getAllocatableBytes(storageUuid)
        return if(imageSize < freeStorage){
            file
        }else{
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "アートワークに必要な容量が足りません。", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }


    private fun registerImage(musicDataBase: MusicDataBase, imageUuid: String, album: String, albumArtist: String){ // save image uuid
        musicDataBase.artworkDao().insert(ArtworkData(
            id = 0,
            imageId = imageUuid,
            album = album,
            artist = albumArtist
        ))
    }

    private fun isSuccessImageSave(image: ByteArray, file: File): Boolean{ // save image and it result
        return try{
            val fileOutputStream = file.outputStream()
            fileOutputStream.write(image)
            fileOutputStream.close()
            true
        }catch (e: Exception){
            false
        }
    }
}