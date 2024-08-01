package com.example.android.playlistmaker.search.data

import com.example.android.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}