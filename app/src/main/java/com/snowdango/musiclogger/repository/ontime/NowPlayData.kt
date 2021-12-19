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
    private val queueData: Map<String, SaveStateData> = mapOf(
        *MusicApp.values().map {
            it.pkg to SaveStateData()
        }.toTypedArray()
    )

    private val mutex: Mutex = Mutex()
    fun setUp(preferences: SharedPreferences) = CoroutineScope(Dispatchers.Default).launch {
        mutex.withLock {
            MusicApp.values().forEach {
                val data = preferences.getQueueData(it.pkg)
                queueData[it.pkg]!!.queueId = data.queueId
                queueData[it.pkg]!!.complete = data.isComplete
                queueData[it.pkg]!!.lastId = data.lastId
                queueData[it.pkg]!!.artwork = data.queueArtwork
            }
            Log.d("queueState", "$queueData")
        }
    }

    private fun isChangeQueue(queueId: Long, packageName: String, isComp: Boolean): Pair<Boolean, Boolean> {
        queueData[packageName]?.let {
            return if (it.queueId == queueId) {
                val isComplete = queueData[packageName]!!.complete
                val pair = Pair(false, isComplete)
                queueData[packageName]!!.complete = isComp
                return pair
            } else {
                it.queueId = queueId
                it.artwork = false
                it.lastId = -1
                it.complete = isComp
                Pair(true, isComp)
            }
        }
        return Pair(true, true)
    }

    suspend fun setLastId(packageName: String, id: Long, queueId: Long) {
        mutex.withLock {
            if (queueId == queueData[packageName]!!.queueId) {
                queueData[packageName]!!.lastId = id
            }
        }
    }

    suspend fun getLastId(packageName: String, queueId: Long): Long {
        mutex.withLock {
            return if (queueId == queueData[packageName]!!.queueId) {
                queueData[packageName]!!.lastId
            } else {
                -1
            }
        }
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

    suspend fun isMusicChange(packageName: String, queueId: Long, isComp: Boolean): Pair<Boolean, Boolean> {
        mutex.withLock {
            return isChangeQueue(queueId, packageName, isComp)
        }
    }

    suspend fun isSaveState(packageName: String, queueId: Long, isComp: Boolean): IsSaveState {
        mutex.withLock {
            val isChange = isChangeQueue(queueId, packageName, isComp)
            val isArtwork = isArtworkSave(queueId, packageName)
            return IsSaveState(isChange.first, isChange.second, isArtwork)
        }
    }

    fun shutDown(preferences: SharedPreferences) = CoroutineScope(Dispatchers.Default).launch {
        mutex.withLock {
            MusicApp.values().forEach {
                preferences.setQueueData(
                    it.pkg,
                    queueData[it.pkg]!!.queueId,
                    queueData[it.pkg]!!.complete,
                    queueData[it.pkg]!!.lastId,
                    queueData[it.pkg]!!.artwork
                )
            }
        }
    }
}

data class SaveStateData(
    var queueId: Long = 0,
    var complete: Boolean = true,
    var lastId: Long = 0,
    var artwork: Boolean = false
)

data class IsSaveState(
    val isChange: Boolean = true,
    val isComplete: Boolean = true,
    val isSaveArtwork: Boolean = false
)