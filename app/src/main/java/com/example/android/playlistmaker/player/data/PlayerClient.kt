package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.TrackTimeState

interface PlayerClient {
    fun doRequest(dto: Any): State
    fun getTime(): TrackTimeState
}