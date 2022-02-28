package com.snowdango.musiclogger.usecase.history

import android.content.Context
import androidx.preference.PreferenceManager
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.dataComplete
import com.snowdango.musiclogger.extention.getLastId
import com.snowdango.musiclogger.extention.saveLastId
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.soywiz.klock.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMusicHistory(
    private val musicDataBase: MusicDataBase,
    private val context: Context
) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    suspend fun saveMusic(musicMeta: MusicMeta) =
        withContext(Dispatchers.IO) {
            val isComp = isCompleteData(musicMeta)
            val id = musicDataBase.musicMetaDao().insert(
                MusicMetadata(
                    title = musicMeta.title,
                    album = musicMeta.album,
                    artist = musicMeta.artist,
                    albumArtist = musicMeta.albumArtist,
                    listeningUnix = DateTime.nowUnixLong(),
                    mediaId = musicMeta.mediaId,
                    app = musicMeta.app
                )
            )
            preferences.saveLastId(id, musicMeta.app)
            if (isComp) preferences.dataComplete(musicMeta.app)
        }

    suspend fun updateMusicData(musicMeta: MusicMeta) =
        withContext(Dispatchers.IO) {
            val isComp = isCompleteData(musicMeta)
            val updateId = preferences.getLastId(musicMeta.app)
            if (updateId != -1L) {
                musicDataBase.musicMetaDao().update(
                    MusicMetadata(
                        id = updateId,
                        title = musicMeta.title,
                        album = musicMeta.album,
                        artist = musicMeta.artist,
                        albumArtist = musicMeta.albumArtist,
                        app = musicMeta.app,
                        mediaId = musicMeta.mediaId,
                        listeningUnix = DateTime.nowUnixLong()
                    )
                )
            }
            if (isComp) preferences.dataComplete(musicMeta.app)
        }

    suspend fun isCompleteData(musicMeta: MusicMeta): Boolean {
        return musicMeta.mediaId != null &&
                musicMeta.album != null &&
                musicMeta.albumArtist != null &&
                musicMeta.artist != null
    }
}