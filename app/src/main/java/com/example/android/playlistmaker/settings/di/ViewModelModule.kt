package com.example.android.playlistmaker.settings.di

import com.example.android.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SettingsViewModel(get())
    }
}