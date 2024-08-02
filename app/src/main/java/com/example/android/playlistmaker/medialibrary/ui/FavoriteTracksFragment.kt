package com.example.android.playlistmaker.medialibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import com.example.android.playlistmaker.medialibrary.presentation.FavoriteState
import com.example.android.playlistmaker.medialibrary.presentation.FavoriteTracksViewModel
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.example.android.playlistmaker.util.TrackConverter
import com.example.android.playlistmaker.util.debounce
import com.example.android.playlistmaker.util.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteTracksFragment()

        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val TRACK_TO_SHOW = "track_to_show"
        private const val TAG = "FavoriteTracksFragment"
    }

    private val viewModel by viewModel<FavoriteTracksViewModel>()
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val adapter = TrackAdapter { track ->
        onTrackClickDebounce(track)
    }


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFavoriteTracksBinding =
        FragmentFavoriteTracksBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            val settingsIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
            val trackConverter: TrackConverter = TrackConverter()
            settingsIntent.putExtra(TRACK_TO_SHOW, trackConverter.map(track))
            startActivity(settingsIntent)
        }
        binding.rvFavoriteTracks.isVisible = false
        binding.grError.isVisible = true

        binding.rvFavoriteTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteTracks.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteState.Loading -> showLoading()
                is FavoriteState.Empty -> showEmpty()
                is FavoriteState.Content -> showContent(state.tracks)
            }
        }
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onStart()
    }

    private fun showLoading() {
        binding.rvFavoriteTracks.isVisible = false
        binding.grError.isVisible = false
    }

    private fun showEmpty() {
        binding.grError.isVisible = true
        binding.rvFavoriteTracks.isVisible = false
    }

    private fun showContent(tracks: List<Track>) {
        binding.grError.isVisible = false
        adapter.updateRecycleView(ArrayList(tracks))
        binding.rvFavoriteTracks.isVisible = true
    }
}