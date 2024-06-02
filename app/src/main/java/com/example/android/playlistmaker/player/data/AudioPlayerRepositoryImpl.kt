package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

class AudioPlayerRepositoryImpl(private val playerClient: PlayerClient) : AudioPlayerRepository {

    override fun controlPlayer(command: Command): State {
        val response = playerClient.doRequest(command)

        return response
    }

    override fun getTime(): Int {
        return playerClient.getTime()
    }
}