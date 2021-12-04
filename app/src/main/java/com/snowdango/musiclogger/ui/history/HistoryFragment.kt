package com.snowdango.musiclogger.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.snowdango.musiclogger.view.history.HistoryScreen
import com.snowdango.musiclogger.viewmodel.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private val viewModel by viewModel<HistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HistoryScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.firstFetch()
    }
}