package com.snowdango.musiclogger.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.snowdango.musiclogger.view.album.AlbumScreen
import com.snowdango.musiclogger.viewmodel.album.AlbumViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment : Fragment() {

    private val viewModel by viewModel<AlbumViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AlbumScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.firstFetch()
    }
}