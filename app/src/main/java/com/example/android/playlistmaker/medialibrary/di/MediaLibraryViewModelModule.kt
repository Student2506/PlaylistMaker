package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.presentation.MediaLibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryViewModelModel = module {
    viewModel {
        MediaLibraryViewModel()
    }
}