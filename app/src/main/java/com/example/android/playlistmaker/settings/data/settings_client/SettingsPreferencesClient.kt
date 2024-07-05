package com.example.android.playlistmaker.settings.data.settings_client

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.example.android.playlistmaker.settings.data.SettingsStorageClient
import com.example.android.playlistmaker.settings.data.dto.ReadSettingsRequest
import com.example.android.playlistmaker.settings.data.dto.Response
import com.example.android.playlistmaker.settings.data.dto.SettingsResponse
import com.example.android.playlistmaker.settings.data.dto.WriteSettingsRequest

class SettingsPreferencesClient(context: Context) : SettingsStorageClient {

    private val sharedPreferences =
        context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)

    override fun doRequest(dto: Any): Response {
        if (dto is ReadSettingsRequest) {
            return SettingsResponse(
                setting_value = sharedPreferences.getBoolean(dto.setting, false)
            )
        } else if (dto is WriteSettingsRequest) {
            sharedPreferences.edit {
                putBoolean(dto.setting, dto.value)
            }
            return Response().apply { resultCode = 0 }
        } else return Response().apply { resultCode = 400 }
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
    }
}