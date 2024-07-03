package com.example.android.playlistmaker.settings.data

import com.example.android.playlistmaker.settings.data.dto.ReadSettingsRequest
import com.example.android.playlistmaker.settings.data.dto.SettingsResponse
import com.example.android.playlistmaker.settings.data.dto.WriteSettingsRequest
import com.example.android.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val settingsStorageClient: SettingsStorageClient) :
    SettingsRepository {
    override fun putDarkTheme(value: Boolean): Int {
        val response =
            settingsStorageClient.doRequest(WriteSettingsRequest(KEY_FOR_NIGHT_THEME, value))
        return response.resultCode
    }

    override fun getDarkTheme(): Boolean {
        val response = settingsStorageClient.doRequest(ReadSettingsRequest(KEY_FOR_NIGHT_THEME))
        return (response as SettingsResponse).setting_value
    }

    companion object {
        const val KEY_FOR_NIGHT_THEME = "key_for_night_theme"
    }
}