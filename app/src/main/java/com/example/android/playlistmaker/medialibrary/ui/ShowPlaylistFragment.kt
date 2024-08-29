package com.example.android.playlistmaker.medialibrary.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.medialibrary.presentation.ShowPlaylistViewModel

class ShowPlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = ShowPlaylistFragment()
    }

    private val viewModel: ShowPlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_show_playlist, container, false)
    }
}