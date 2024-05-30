package com.example.android.playlistmaker.player.domain.impl

import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import java.util.concurrent.Executors

class AudioPlayerInteractorImpl(private val player: AudioPlayerRepository) : AudioPlayerInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun controlPlayer(
        request: Command,
        consumer: AudioPlayerInteractor.AudioPlayerConsumer,
    ) {
        executor.execute {
            consumer.consume(player.controlPlayer(request))
        }
    }

    override fun getTrackTime(timer: AudioPlayerInteractor.AudioPlayerTrackTimeConsumer) {
        executor.execute {
            timer.getTime(player.getTrackStatus())
        }
    }
}