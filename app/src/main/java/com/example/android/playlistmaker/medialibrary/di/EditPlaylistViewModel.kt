package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val editPlaylistViewModel = module {
    viewModel { (playlist: Long) ->
        EditPlaylistViewModel(playlist, get(), get())
    }
}