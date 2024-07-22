package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {
    fun controlPlayer(command: Command): Flow<State>
    fun getTime(): Flow<State>
}