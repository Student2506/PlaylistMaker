package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.search.domain.models.Track

interface AudioPlayerInteractor {
    fun startPlayer(consumer: AudioPlayerConsumer)

    fun pausePlayer(consumer: AudioPlayerConsumer)

    fun playbackControl(consumer: AudioPlayerConsumer)

    fun preparePlayer(trackUrl: String, consumer: AudioPlayerConsumer)

    fun getTrackTime(consumer: AudioPlayerConsumer): Int

    interface AudioPlayerConsumer {
        fun consume(status: State)
    }
}