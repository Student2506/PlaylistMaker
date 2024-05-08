package com.example.android.playlistmaker.domain.impl

import android.media.MediaPlayer
import android.util.Log
import com.example.android.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.domain.api.AudioPlayerInteractor.STATE

class AudioPlayerInteractorImpl : AudioPlayerInteractor {

    private val mediaPlayer = MediaPlayer()
    private var current_track: String? = null

    override var statePlayer: STATE = STATE.STATE_DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        statePlayer = STATE.STATE_PLAYING
    }

    override fun pausePlayer() {
        if (statePlayer == STATE.STATE_PLAYING) mediaPlayer.pause()
        statePlayer = STATE.STATE_PAUSED
    }

    override fun playbackControl(): Boolean {
        Log.d(TAG, statePlayer.toString())
        when (statePlayer) {
            STATE.STATE_PLAYING -> {
                pausePlayer()
                return true
            }

            STATE.STATE_PREPARED, STATE.STATE_PAUSED -> {
                startPlayer()
                return false
            }

            STATE.STATE_DEFAULT -> throw IllegalStateException("Player is not ready!")
        }
    }

    override fun preparePlayer(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletePlay: () -> Unit,
    ) {
        if (current_track != trackUrl) {
            current_track = trackUrl
            mediaPlayer.reset()
        }
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            statePlayer = STATE.STATE_PAUSED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            statePlayer = STATE.STATE_PREPARED
            onCompletePlay()
        }
    }

    override fun release() {
        mediaPlayer.release()
        statePlayer = STATE.STATE_DEFAULT
    }

    override val currentPosition: Int
        get() = if (statePlayer != STATE.STATE_DEFAULT) mediaPlayer.currentPosition else 0


    companion object {
        private final val TAG = "AudioPlayerInteractorImpl"
    }
}