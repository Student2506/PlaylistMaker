package com.example.android.playlistmaker.medialibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.android.playlistmaker.medialibrary.presentation.FavoriteTracksViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private val viewModel: FavoriteTracksViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFavoriteTracksBinding {
        return FragmentFavoriteTracksBinding.inflate(inflater, container, false)
    }
}