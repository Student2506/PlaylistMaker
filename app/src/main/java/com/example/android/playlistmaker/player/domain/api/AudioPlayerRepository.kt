package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

interface AudioPlayerRepository {
    fun controlPlayer(command: Command): State
    fun getTrackStatus(): Int
}