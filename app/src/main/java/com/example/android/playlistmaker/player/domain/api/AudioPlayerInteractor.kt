package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import kotlinx.coroutines.flow.Flow

interface AudioPlayerInteractor {
    fun controlPlayer(request: Command): Flow<State>
    fun getTrackTime(): Flow<State>
}