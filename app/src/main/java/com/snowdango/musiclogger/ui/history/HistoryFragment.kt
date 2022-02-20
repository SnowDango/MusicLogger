package com.snowdango.musiclogger.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.snowdango.musiclogger.App
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.databinding.FragmentBaseBinding
import com.snowdango.musiclogger.viewmodel.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private val viewModel by viewModel<HistoryViewModel>()
    private var binding: FragmentBaseBinding? = null
    private val artworkSize: Int by lazy { ((App.deviceMaxWidth / 6) * App.density).toInt() }
    private val controller: HistoryEpoxyController by lazy { HistoryEpoxyController(artworkSize) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseBinding.inflate(layoutInflater, container, false)

        binding!!.includeToolbar.root.inflateMenu(R.menu.setting_menu)

        binding!!.recyclerView.adapter = controller.adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.firstFetch()
        viewModel.historyData.observe(viewLifecycleOwner) {
            controller.setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}