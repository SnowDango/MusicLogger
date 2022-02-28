package com.snowdango.musiclogger.usecase.query

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.snowdango.musiclogger.extention.QueryPreferences
import com.snowdango.musiclogger.extention.getQuery
import com.snowdango.musiclogger.extention.setQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class MusicQueryState(private val context: Context) : KoinComponent {
    
    private val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    suspend fun checkChangePlaying(queueId: Long, appString: String): QueryState = withContext(Dispatchers.IO) {
        val lastQuery = preferences.getQuery(appString)
        if (lastQuery.queueId == queueId) { // musicが変わってない時
            return@withContext if (lastQuery.isComplete) {
                if (lastQuery.isSaveArtwork) {
                    QueryState.COMPLETE
                } else {
                    QueryState.NOT_ARTWORK
                }
            } else {
                if (lastQuery.isSaveArtwork) {
                    QueryState.NOT_COMP
                } else {
                    QueryState.NOT_ARTWORK_AND_NOT_COMP
                }
            }
        } else {
            val queryPreferences = QueryPreferences(queueId, false, false)
            preferences.setQuery(appString, queryPreferences) //
            return@withContext QueryState.CHANGED
        }
    }

    enum class QueryState {
        COMPLETE,
        NOT_ARTWORK,
        NOT_COMP,
        CHANGED,
        NOT_ARTWORK_AND_NOT_COMP
    }
}