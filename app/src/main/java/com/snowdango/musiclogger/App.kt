package com.snowdango.musiclogger

import android.app.Application
import androidx.room.Room
import com.snowdango.musiclogger.model.service.MusicServiceModel
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.service.MusicNotifyListenerService
import com.snowdango.musiclogger.ui.MainActivity
import com.snowdango.musiclogger.usecase.MusicSessionState
import com.snowdango.musiclogger.usecase.SaveArtwork
import com.snowdango.musiclogger.usecase.SaveMusic
import com.snowdango.musiclogger.usecase.SessionData
import com.snowdango.musiclogger.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(mainModule,serviceModule))
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }

    private val mainModule = module {
        //ui
        factory { MainActivity() }
        //viewModel
        viewModel { MainViewModel() }
    }

    private val serviceModule = module {
        //service
        factory { MusicNotifyListenerService() }
        //model
        factory { MusicServiceModel(get(),get(),get(),get(),get()) }
        //usecases
        factory { SessionData() }
        factory { MusicSessionState(get()) }
        factory { SaveArtwork(get(),get()) }
        factory { SaveMusic(get(), get()) }
        // DB
        factory { Room.databaseBuilder(get(),MusicDataBase::class.java, "music_log").build() }
    }

}