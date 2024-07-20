package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

interface AudioPlayerInteractor {
    fun controlPlayer(request: Command, consumer: AudioPlayerConsumer)

    fun getTrackTime(timer: AudioPlayerTrackTimeConsumer)

    interface AudioPlayerConsumer {
        fun consume(status: State)
    }

    interface AudioPlayerTrackTimeConsumer {
        fun getTime(state: State)
    }
}