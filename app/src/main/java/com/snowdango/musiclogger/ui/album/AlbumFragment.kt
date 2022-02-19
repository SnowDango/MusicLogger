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
    private var binding: FragmentBaseBinding? = null
    private var controller: AlbumEpoxyController? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBaseBinding.inflate(inflater, container, false)

        binding!!.includeToolbar.root.inflateMenu(R.menu.setting_menu)

        val artworkSize = ((App.deviceMaxWidth / 4) * App.density).toInt()
        controller = AlbumEpoxyController(artworkSize)
        binding!!.recyclerView.adapter = controller!!.adapter
        binding!!.recyclerView.layoutManager = GridLayoutManager(context, 2)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.firstFetch()
        viewModel.albumData.observe(viewLifecycleOwner) {
            controller!!.setData(it)
        }
    }
}