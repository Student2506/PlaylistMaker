package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.android.playlistmaker.medialibrary.presentation.FavoriteTracksViewModel
import com.example.android.playlistmaker.medialibrary.presentation.SelectPage

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}