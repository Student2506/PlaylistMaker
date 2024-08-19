package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.presentation.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModelModule = module {
    viewModel {
        PlaylistViewModel(get())
    }
}