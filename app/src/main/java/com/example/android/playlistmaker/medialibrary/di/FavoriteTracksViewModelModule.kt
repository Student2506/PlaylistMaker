package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.presentation.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteTracksViewModel = module {
    viewModel {
        FavoriteTracksViewModel(get())
    }
}