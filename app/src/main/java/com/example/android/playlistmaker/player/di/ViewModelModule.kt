package com.example.android.playlistmaker.player.di

import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get())
    }
}