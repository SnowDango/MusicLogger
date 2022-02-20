package com.snowdango.musiclogger.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.snowdango.musiclogger.App
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.databinding.FragmentBaseBinding
import com.snowdango.musiclogger.viewmodel.album.AlbumViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment : Fragment() {

    private val viewModel by viewModel<AlbumViewModel>()
    private val binding: FragmentBaseBinding by lazy { FragmentBaseBinding.inflate(layoutInflater) }
    private val controller: AlbumEpoxyController by lazy { AlbumEpoxyController(((App.deviceMaxWidth / 4) * App.density).toInt()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.includeToolbar.root.inflateMenu(R.menu.setting_menu)

        binding.recyclerView.adapter = controller.adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.firstFetch()
        viewModel.albumData.observe(viewLifecycleOwner) {
            controller.setData(it)
        }
    }
}