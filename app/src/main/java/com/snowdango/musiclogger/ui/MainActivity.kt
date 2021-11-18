package com.snowdango.musiclogger.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import androidx.core.app.NotificationManagerCompat
import com.snowdango.musiclogger.viewmodel.MainViewModel
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.connection.ViewObject
import com.snowdango.musiclogger.service.MusicNotifyListenerService

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }else{
            val intent = Intent(this, MusicNotifyListenerService::class.java)
            startForegroundService(intent)
        }
        setData()
    }

    private fun setData(){
        ViewObject.art.observe(this, {
            val image = findViewById<ImageView>(R.id.imageView)
            it?.let {
                image.setImageBitmap(it)
            }
        })
    }
}
