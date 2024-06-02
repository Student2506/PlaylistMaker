package com.example.android.playlistmaker.search.data

import com.example.android.playlistmaker.search.data.dto.SharedPreferencesRequest
import com.example.android.playlistmaker.search.data.dto.SharedPreferencesResponse
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesRepository

class SharedPreferncesRepositoryImpl(private val settingsStorageClient: SettingsStorageClient) :
    SharedPreferencesRepository {
    override fun putValue(term: String, value: Any): Int {
        val response = settingsStorageClient.doRequest(SharedPreferencesRequest(term, value))
        return response.resultCode
    }

    override fun getValue(term: String): Any {
        val response = settingsStorageClient.doRequest(SharedPreferencesRequest(term, null))
        if (response.resultCode == 0) {
            return (response as SharedPreferencesResponse).value
        }
        return ""
    }
}