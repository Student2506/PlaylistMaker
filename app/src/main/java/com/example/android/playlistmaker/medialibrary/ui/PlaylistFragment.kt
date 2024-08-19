package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.presentation.PlaylistState
import com.example.android.playlistmaker.medialibrary.presentation.PlaylistTileAdapter
import com.example.android.playlistmaker.medialibrary.presentation.PlaylistViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private val viewModel by viewModel<PlaylistViewModel>()
    private val playlist: MutableList<Playlist> = mutableListOf()
    private var adapter: PlaylistTileAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mbNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.createPlaylistFragment)
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.Content -> showContent(state.playlist)
                is PlaylistState.Empty -> showEmpty()
            }
        }
        binding.rvPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PlaylistTileAdapter(playlist)
        binding.rvPlaylist.adapter = adapter!!

        viewModel.setPlaylists()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setPlaylists()
    }

    private fun showEmpty() {
        binding.grNoPlaylist.isVisible = true
        binding.rvPlaylist.isVisible = false
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.grNoPlaylist.isVisible = false
        playlist.clear()
        playlist.addAll(playlists)
        adapter?.notifyDataSetChanged()
        binding.rvPlaylist.isVisible = true
    }


}