package com.example.android.playlistmaker.player.data.util

import com.example.android.playlistmaker.player.data.dto.StateDto
import com.example.android.playlistmaker.player.domain.models.State

class StateMapper {
    companion object {
        fun toState(stateDto: StateDto): State {
            return when (stateDto) {
                is StateDto.Playing -> State.STATE_PLAYING
                is StateDto.Paused -> State.STATE_PAUSED
                is StateDto.Prepared -> State.STATE_PAUSED
                is StateDto.Default -> State.STATE_DEFAULT
            }
        }
    }
}