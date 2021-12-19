package com.snowdango.musiclogger

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.snowdango.musiclogger.model.HistoryAlbumModel
import com.snowdango.musiclogger.model.MusicHistoryModel
import com.snowdango.musiclogger.model.MusicServiceModel
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.service.MusicNotifyListenerService
import com.snowdango.musiclogger.ui.MainActivity
import com.snowdango.musiclogger.ui.album.AlbumFragment
import com.snowdango.musiclogger.ui.history.HistoryFragment
import com.snowdango.musiclogger.usecase.*
import com.snowdango.musiclogger.viewmodel.MainViewModel
import com.snowdango.musiclogger.viewmodel.album.AlbumViewModel
import com.snowdango.musiclogger.viewmodel.history.HistoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    companion object {
        var preferences: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        configureTimber()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(listOf(mainModule, historyModule, albumModule, singletonModule, usecaseModule, serviceModule))
        }
    }

    override fun onTerminate() {
        stopKoin()
        preferences = null
        super.onTerminate()
    }

    private fun configureTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private val singletonModule = module {
        single { Room.databaseBuilder(get(), MusicDataBase::class.java, "music_log").build() }
    }

    private val usecaseModule = module {
        factory { LoadMusicHistory(get()) }
        factory { MoreLoadMusicHistory(get()) }
        factory { UpdateLoadMusicHistory(get()) }
        factory { LoadAlbum(get()) }
        factory { MoreLoadAlbum(get()) }
        factory { SessionData() }
        factory { MusicQueryState() }
        factory { SaveArtwork(get(), get()) }
        factory { SaveMusicHistory(get()) }
        factory { UpdateMusicData(get()) }
    }

    private val mainModule = module {
        //ui
        factory { MainActivity() }
        //viewModel
        viewModel { MainViewModel() }
        //model
        factory { MusicHistoryModel() }
    }

    private val historyModule = module {
        // ui
        factory { HistoryFragment() }
        // viewModel
        viewModel { HistoryViewModel() }
        // model
        factory { MusicHistoryModel() }
    }

    private val albumModule = module {
        // ui
        factory { AlbumFragment() }
        // viewModel
        viewModel { AlbumViewModel() }
        // model
        factory { HistoryAlbumModel() }
    }

    private val serviceModule = module {
        //service
        factory { MusicNotifyListenerService() }
        //model
        factory { MusicServiceModel(get()) }
    }
}