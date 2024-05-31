package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.data.dto.Response

interface PlayerClient {
    fun doRequest(dto: Any): Response
    fun getTime(): Int
}