package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.data.dto.PlayerRequest
import com.example.android.playlistmaker.player.data.dto.PlayerResponse
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

class AudioPlayerRepositoryImpl(private val playerClient: PlayerClient) : AudioPlayerRepository {

    override fun controlPlayer(command: Command): State {
        val response = playerClient.doRequest(PlayerRequest(command))
        if (response.resultCode == 0) {
            return (response as PlayerResponse).stateDto
        } else {
            return State.Default
        }
    }

    override fun getTime(): Int {
        return playerClient.getTime()
    }
}