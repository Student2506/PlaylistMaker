package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.domain.models.State

interface PlayerClient {
    fun doRequest(dto: Any): State
    fun getTime(): State
}