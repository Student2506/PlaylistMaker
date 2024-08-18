package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.presentation.CreatePlaylistViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createPlaylistViewModelModule = module {
    viewModel {
        CreatePlaylistViewModel(androidApplication(), get())
    }
}