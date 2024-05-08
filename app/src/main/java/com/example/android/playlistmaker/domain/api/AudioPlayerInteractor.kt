package com.example.android.playlistmaker.domain.api

interface AudioPlayerInteractor {

    enum class STATE {
        STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED
    }

    var statePlayer: STATE

    abstract fun startPlayer()

    abstract fun pausePlayer()

    abstract fun playbackControl(): Boolean

    abstract fun preparePlayer(trackUrl: String, onPrepared: () -> Unit, onCompletePlay: () -> Unit)

    abstract fun release()

    abstract val currentPosition: Int
}