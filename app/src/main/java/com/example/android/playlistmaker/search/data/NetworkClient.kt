package com.example.android.playlistmaker.search.data

import com.example.android.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}