package com.snowdango.musiclogger.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.snowdango.musiclogger.App
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.databinding.FragmentBaseBinding
import com.snowdango.musiclogger.viewmodel.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private val viewModel by viewModel<HistoryViewModel>()
    private var _binding: FragmentBaseBinding? = null
    private val binding get() = _binding!!
    private var _controller: HistoryEpoxyController? = null
    private val controller get() = _controller!!
    private val artworkSize: Int by lazy { ((App.deviceMaxWidth / 6) * App.density).toInt() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseBinding.inflate(layoutInflater, container, false)
        _controller = HistoryEpoxyController(artworkSize)

        binding.includeToolbar.root.inflateMenu(R.menu.setting_menu)

        binding.recyclerView.adapter = controller.adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.firstFetch()
        viewModel.historyData.observe(viewLifecycleOwner) {
            controller.setData(it)
        }
        App.analytics?.let {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "History")
            it.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _controller = null
    }

}