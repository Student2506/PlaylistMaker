package com.example.android.playlistmaker.settings.di

import com.example.android.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.android.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val settingsInteractorModule = module {
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}