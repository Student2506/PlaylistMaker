package com.example.android.playlistmaker.player.domain.impl

import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

class AudioPlayerInteractorImpl(private val player: AudioPlayerRepository) : AudioPlayerInteractor {
    override fun startPlayer() {
        player.controlPlayer(Command.Play)
    }

    override fun pausePlayer() {
        player.controlPlayer(Command.Pause)
    }

    override fun playbackControl(): State {
        return player.controlPlayer(Command.PlayPause)
    }

    override fun preparePlayer(
        trackUrl: String,
    ) {
        player.controlPlayer(Command.Prepare(trackUrl))
    }

    override fun getTrackTime(): Int {
        return player.getTrackStatus()
    }

}