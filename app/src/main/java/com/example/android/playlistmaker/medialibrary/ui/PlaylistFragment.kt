package com.example.android.playlistmaker.medialibrary.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.playlistmaker.medialibrary.presentation.PlaylistViewModel
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentPlaylistBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}