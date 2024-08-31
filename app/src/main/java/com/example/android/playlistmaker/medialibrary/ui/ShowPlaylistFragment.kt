package com.example.android.playlistmaker.medialibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentShowPlaylistBinding
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import com.example.android.playlistmaker.medialibrary.presentation.ShowPlaylistViewModel
import com.example.android.playlistmaker.medialibrary.presentation.TrackAdapter
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.example.android.playlistmaker.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.example.android.playlistmaker.search.ui.SearchFragment.Companion.TRACK_TO_SHOW
import com.example.android.playlistmaker.util.TrackConverter
import com.example.android.playlistmaker.util.debounce
import com.example.android.playlistmaker.util.ui.BindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ShowPlaylistFragment : BindingFragment<FragmentShowPlaylistBinding>() {

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist_id"
        private const val TAG = "ShowPlaylistFragment"

        fun createArgs(playlistId: Long): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }

    private val playlist by lazy { requireArguments().getLong(ARGS_PLAYLIST_ID) }
    private val viewModel: ShowPlaylistViewModel by viewModel {
        parametersOf(playlist)
    }
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var confirmDialog: MaterialAlertDialogBuilder? = null

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private val adapter = TrackAdapter(object : TrackAdapter.TrackClickListener {
        override fun onTrackClick(track: Track) {
            onTrackClickDebounce(track)
        }

        override fun onTrackLongClick(track: Track): Boolean {
            confirmDialog =
                MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.remove_track_title)
                    .setMessage(R.string.remove_track_message)
                    .setPositiveButton(getString(R.string.confirm_remove_option)) { _, _ ->
                        viewModel.requestToRemoveTrackFromPlaylist(playlist, track.trackId)
                    }.setNegativeButton(getString(R.string.cancel_option)) { _, _ ->
                    }
            confirmDialog?.show()
            return true
        }
    })

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentShowPlaylistBinding = FragmentShowPlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.rvPlaylist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlaylist.adapter = adapter
        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            showTrack(track)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
        viewModel.observeState().observe(viewLifecycleOwner) { playlist ->
            if (playlist.tracks != null) adapter.updateRecycleView(
                viewModel.tracksToPlaylistTracks(
                    playlist.tracks
                )
            )
            Glide.with(requireContext()).load(playlist.imageUrl).placeholder(R.drawable.placeholder)
                .into(binding.ivPlaylistImage)
            binding.tvPlaylistName.text = playlist.title
            binding.tvPlaylistDescription.text =
                playlist.description ?: getString(R.string.no_description)

            if (playlist.tracks != null) {
                var totalLength = 0L
                for (track in playlist.tracks) {
                    totalLength += track.trackTime
                }
                val total = totalLength / 60000
                binding.tvTotalPLLength.text = MinuteCount(total)
                binding.tvTotalPLQty.text = viewModel.TrackCount(playlist.tracks.size)
            } else {
                binding.tvTotalPLLength.text = MinuteCount(0L)
                binding.tvTotalPLQty.text = viewModel.TrackCount(0)
            }
        }
        binding.tbToolbar.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivShare.setOnClickListener {
            val message = viewModel.buildMessage()
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareAppIntent)
        }
    }


    private fun MinuteCount(trackQty: Long): String {
        if (trackQty % 100 in 5L..20L) return "$trackQty минут"
        if (trackQty % 10 == 1L) return "$trackQty минута"
        if (trackQty % 10 in 2L..4L) return "$trackQty минуты"
        else return "$trackQty треков"
    }

    private fun showTrack(track: Track) {
        val playertIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
        playertIntent.putExtra(TRACK_TO_SHOW, TrackConverter().map(track))
        startActivity(playertIntent)
    }
}