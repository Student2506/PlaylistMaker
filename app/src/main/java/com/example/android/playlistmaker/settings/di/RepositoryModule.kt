package com.example.android.playlistmaker.settings.di

import com.example.android.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.android.playlistmaker.settings.domain.api.SettingsRepository
import org.koin.dsl.module

val settingsRepositoryModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}
    