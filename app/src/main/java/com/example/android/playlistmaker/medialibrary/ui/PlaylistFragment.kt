package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.android.playlistmaker.medialibrary.presentation.PlaylistViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private val viewModel: PlaylistViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

}