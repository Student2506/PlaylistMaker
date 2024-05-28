package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.State

interface AudioPlayerInteractor {
    fun startPlayer(consumer: AudioPlayerConsumer)

    fun pausePlayer(consumer: AudioPlayerConsumer)

    fun playbackControl(consumer: AudioPlayerConsumer)

    fun preparePlayer(trackUrl: String, consumer: AudioPlayerConsumer)

    fun getTrackTime(timer: AudioPlayerTrackTimeConsumer)

    fun release(consumer: AudioPlayerConsumer)

    interface AudioPlayerConsumer {
        fun consume(status: State)
    }

    interface AudioPlayerTrackTimeConsumer {
        fun getTime(time: Int)
    }
}