package com.example.android.playlistmaker.domain.api

import android.widget.ImageButton
import android.widget.TextView

interface AudioPlayerInteractor {

    enum class STATE {
        STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED
    }

    var statePlayer: STATE

    abstract fun startPlayer()

    abstract fun pausePlayer()

    abstract fun playbackControl(): Boolean

    abstract fun preparePlayer(trackUrl: String, button: ImageButton, elapsedTime: TextView)

    abstract fun release()
}