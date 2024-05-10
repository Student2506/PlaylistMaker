package com.example.android.playlistmaker.data

import android.media.MediaPlayer
import android.util.Log
import com.example.android.playlistmaker.data.player.MediaPlayerClient.STATE
import com.example.android.playlistmaker.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.domain.models.State

class MediaPlayerClientImpl : AudioPlayerRepository {


    private val mediaPlayer = MediaPlayer()
    private var current_track: String? = null

    private var statePlayer = STATE.STATE_DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        statePlayer = STATE.STATE_PLAYING
    }

    override fun pausePlayer() {
        if (statePlayer == STATE.STATE_PLAYING) mediaPlayer.pause()
        statePlayer = STATE.STATE_PAUSED
    }

    override fun playbackControl(): State {
        Log.d(TAG, statePlayer.toString())
        when (statePlayer) {
            STATE.STATE_PLAYING -> {
                pausePlayer()
                return State.STATE_PAUSED
            }

            STATE.STATE_PREPARED, STATE.STATE_PAUSED -> {
                startPlayer()
                return State.STATE_PLAYING
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