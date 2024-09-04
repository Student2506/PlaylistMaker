package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.presentation.ShowPlaylistViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val showPlaylistViewModel = module {
    viewModel { (playlist: Long) ->
        ShowPlaylistViewModel(playlist, get(), get(), androidApplication())
    }
}