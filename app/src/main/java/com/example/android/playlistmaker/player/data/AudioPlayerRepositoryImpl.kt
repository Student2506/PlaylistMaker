package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioPlayerRepositoryImpl(private val playerClient: PlayerClient) : AudioPlayerRepository {

    override fun controlPlayer(command: Command): State {
        val response = playerClient.doRequest(command)

        return response
    }

    override fun getTime(): Flow<State> = flow {
        emit(playerClient.getTime())
    }
}