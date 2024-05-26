package com.example.android.playlistmaker.player.domain.impl

import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import java.util.concurrent.Executors

class AudioPlayerInteractorImpl(private val player: AudioPlayerRepository) : AudioPlayerInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun startPlayer(consumer: AudioPlayerInteractor.AudioPlayerConsumer) {
        executor.execute {
            consumer.consume(player.controlPlayer(Command.Play))
        }
    }

    override fun pausePlayer(consumer: AudioPlayerInteractor.AudioPlayerConsumer) {
        executor.execute {
            consumer.consume(player.controlPlayer(Command.Pause))
        }
    }

    override fun playbackControl(consumer: AudioPlayerInteractor.AudioPlayerConsumer) {
        executor.execute {
            consumer.consume(player.controlPlayer(Command.PlayPause))
        }
    }

    override fun preparePlayer(
        trackUrl: String,
        consumer: AudioPlayerInteractor.AudioPlayerConsumer,
    ) {
        executor.execute {
            consumer.consume(player.controlPlayer(Command.Prepare(trackUrl)))
        }
    }

    override fun getTrackTime(timer: AudioPlayerInteractor.AudioPlayerTrackTimeConsumer) {
        executor.execute {
            timer.getTime(player.getTrackStatus())
        }
    }
}