package com.snowdango.musiclogger.repository.ontime

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snowdango.musiclogger.domain.session.MusicApp
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.getQueueData
import com.snowdango.musiclogger.extention.setQueueData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object NowPlayData {

    val musicMeta = MutableLiveData<MusicMeta>()
    private val queueData: Map<String,SaveStateData> = mapOf(
        *MusicApp.values().map {
            it.pkg to SaveStateData()
        }.toTypedArray()
    )
    private val mutex: Mutex = Mutex()

    fun setUp(preferences: SharedPreferences) = CoroutineScope(Dispatchers.Default).launch{
        mutex.withLock {
            MusicApp.values().forEach {
                val data = preferences.getQueueData(it.pkg)
                queueData[it.pkg]!!.queueId = data.first
                queueData[it.pkg]!!.artwork = data.second
            }
            Log.d("queueState", "$queueData")
        }
    }

    private fun isChangeQueue(queueId: Long, packageName: String): Boolean{
        queueData[packageName]?.let {
            return if (it.queueId == queueId) {
                false
            } else {
                it.queueId = queueId
                it.artwork = false
                true
            }
        }
        return true
    }

    private fun isArtworkSave(queueId: Long, packageName: String): Boolean {
        queueData[packageName]?.let {
            if (it.queueId == queueId) {
                if (!it.artwork) {
                    it.artwork = true
                    return false
                }
            }
        }
        return true
    }

    suspend fun isMusicChange(packageName: String, queueId: Long): Boolean{
        mutex.withLock {
            return isChangeQueue(queueId, packageName)
        }
    }

    suspend fun isSaveState(packageName: String, queueId: Long): IsSaveState{
        mutex.withLock {
            val isChange = isChangeQueue(queueId, packageName)
            val isArtwork = isArtworkSave(queueId, packageName)
            return IsSaveState(isChange, isArtwork)
        }
    }

    fun shutDown(preferences: SharedPreferences) = CoroutineScope(Dispatchers.Default).launch{
        mutex.withLock {
            MusicApp.values().forEach {
                preferences.setQueueData(it.pkg, queueData[it.pkg]!!.queueId, queueData[it.pkg]!!.artwork)
            }
        }
    }
}

data class SaveStateData(
    var queueId: Long = 0,
    var artwork: Boolean = false
)

data class IsSaveState(
    val isChange: Boolean = true,
    val isSaveArtwork: Boolean = false
)