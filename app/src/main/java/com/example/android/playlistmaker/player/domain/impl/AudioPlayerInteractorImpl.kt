package com.example.android.playlistmaker.player.domain.impl

import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.State

class AudioPlayerInteractorImpl(private val player: AudioPlayerRepository) : AudioPlayerInteractor {
    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun playbackControl(): State {
        return player.playbackControl()
    }

    override fun preparePlayer(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletePlay: () -> Unit,
    ) {
        player.preparePlayer(trackUrl, onPrepared, onCompletePlay)
    }

    override fun release() {
        player.release()
    }

    override fun getTrackTime(): Int {
        return player.currentPosition
    }

}