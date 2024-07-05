package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.TrackTimeState

interface AudioPlayerRepository {
    fun controlPlayer(command: Command): State
    fun getTime(): TrackTimeState
}