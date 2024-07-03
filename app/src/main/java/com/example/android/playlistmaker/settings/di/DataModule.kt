package com.example.android.playlistmaker.settings.di

import com.example.android.playlistmaker.settings.data.SettingsStorageClient
import com.example.android.playlistmaker.settings.data.settings_client.SettingsPreferencesClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDataModule = module {
    single<SettingsStorageClient> {
        SettingsPreferencesClient(androidContext())
    }
}