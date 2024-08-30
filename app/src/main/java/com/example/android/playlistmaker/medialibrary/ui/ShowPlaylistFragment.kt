package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentShowPlaylistBinding
import com.example.android.playlistmaker.medialibrary.presentation.ShowPlaylistViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

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

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentShowPlaylistBinding = FragmentShowPlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
        viewModel.observeState().observe(viewLifecycleOwner) { playlist ->
            Glide.with(requireContext()).load(playlist.imageUrl).placeholder(R.drawable.placeholder)
                .into(binding.ivPlaylistImage)
            binding.tvPlaylistName.text = playlist.title
            binding.tvPlaylistDescription.text = playlist.description ?: ""

            if (playlist.tracks != null) {
                var totalLength = 0L
                for (track in playlist.tracks) {
                    totalLength += track.trackTime
                }
                val total = totalLength / 60000
                binding.tvTotalPLLength.text = MinuteCount(total)
                binding.tvTotalPLQty.text = TrackCount(playlist.tracks.size)
            } else {
                binding.tvTotalPLLength.text = MinuteCount(0L)
                binding.tvTotalPLQty.text = TrackCount(0)
            }
        }
        binding.tbToolbar.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun TrackCount(trackQty: Int): String {
        if (trackQty % 100 in 5..20) return "$trackQty треков"
        if (trackQty % 10 == 1) return "$trackQty трек"
        if (trackQty % 10 in 2..4) return "$trackQty трека"
        else return "$trackQty треков"
    }

    private fun MinuteCount(trackQty: Long): String {
        if (trackQty % 100 in 5L..20L) return "$trackQty минут"
        if (trackQty % 10 == 1L) return "$trackQty минута"
        if (trackQty % 10 in 2L..4L) return "$trackQty минуты"
        else return "$trackQty треков"
    }
}