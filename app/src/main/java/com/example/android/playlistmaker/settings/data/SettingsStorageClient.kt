package com.example.android.playlistmaker.settings.data

import com.example.android.playlistmaker.settings.data.dto.Response

interface SettingsStorageClient {
    fun doRequest(dto: Any): Response
}