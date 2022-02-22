package com.snowdango.musiclogger.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.databinding.ActivityMainBinding
import com.snowdango.musiclogger.service.MusicNotifyListenerService
import com.snowdango.musiclogger.viewmodel.MainViewModel
import com.snowdango.musiclogger.worker.ArtworkSaveWorker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Duration

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // start service
        startService()
        startWorker()
    }

    override fun onResume() {
        super.onResume()
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        binding.navigationBottom.setupWithNavController(navController)
    }

    private fun startService() {
        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        } else {
            val intent = Intent(this, MusicNotifyListenerService::class.java)
            startForegroundService(intent)
        }
    }

    private fun startWorker() {
        val worker_tag = "ArtworkSaveWorker"
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .setRequiresBatteryNotLow(true)
            .build()
        val request = PeriodicWorkRequest.Builder(
            ArtworkSaveWorker::class.java,
            Duration.ofHours(1)
        ).setConstraints(constraints).addTag(worker_tag).build()
        WorkManager.getInstance(this).enqueue(request)
    }
    
}
