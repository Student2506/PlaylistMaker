package com.example.android.playlistmaker.search.data

import com.example.android.playlistmaker.search.data.dto.GetHistoryTracksRequest
import com.example.android.playlistmaker.search.data.dto.HistoryTracksResponse
import com.example.android.playlistmaker.search.data.dto.PutHistoryTracksRequest
import com.example.android.playlistmaker.search.domain.api.HistoryTracksRepository

class HistoryTracksRepositoryImpl(private val settingsStorageClient: SettingsStorageClient) :
    HistoryTracksRepository {

    override fun putHistoryTracks(term: String, value: String): Int {
        val response = settingsStorageClient.doRequest(PutHistoryTracksRequest(term, value))
        return response.resultCode
    }

    override fun getHistoryTracks(term: String): String {
        val response = settingsStorageClient.doRequest(GetHistoryTracksRequest(term))
        return (response as HistoryTracksResponse).value
    }
}