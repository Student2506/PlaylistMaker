package com.example.android.playlistmaker.search.di

import com.example.android.playlistmaker.search.presentation.TrackSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        TrackSearchViewModel(get(), get())
    }
}