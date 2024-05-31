package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.data.dto.PlayerRequest
import com.example.android.playlistmaker.player.data.dto.PlayerResponse
import com.example.android.playlistmaker.player.data.util.CommandMapper
import com.example.android.playlistmaker.player.data.util.StateMapper
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

class AudioPlayerRepositoryImpl(private val playerClient: PlayerClient) : AudioPlayerRepository {

    override fun controlPlayer(command: Command): State {
        val response = playerClient.doRequest(PlayerRequest(CommandMapper.toCommandDto(command)))
        if (response.resultCode == 0) {
            return StateMapper.toState((response as PlayerResponse).stateDto)
        } else {
            return State.STATE_DEFAULT
        }
    }

    override fun getTime(): Int {
        return playerClient.getTime()
    }
}