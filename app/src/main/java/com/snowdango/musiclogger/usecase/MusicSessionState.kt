package com.snowdango.musiclogger.usecase

import android.content.Context
import androidx.preference.PreferenceManager
import com.snowdango.musiclogger.extention.getSession
import com.snowdango.musiclogger.extention.setSession

object MusicSessionState {

    fun isPlaySongChanged(context: Context, packageName: String, sessionId: Long): Boolean{
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        val recentSessionId = preference.getSession(packageName)
        return if(sessionId == recentSessionId){
            false
        }else{
            preference.setSession(packageName, sessionId)
            true
        }
    }

}