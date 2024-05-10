package com.example.android.playlistmaker.data

import android.media.MediaPlayer
import android.util.Log
import com.example.android.playlistmaker.data.dto.StateDto
import com.example.android.playlistmaker.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.domain.models.State

class MediaPlayerClientImpl : AudioPlayerRepository {


    private val mediaPlayer = MediaPlayer()
    private var current_track: String? = null

    private var statePlayer = StateDto.STATE_DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        statePlayer = StateDto.STATE_PLAYING
    }

    override fun pausePlayer() {
        if (statePlayer == StateDto.STATE_PLAYING) mediaPlayer.pause()
        statePlayer = StateDto.STATE_PAUSED
    }

    override fun playbackControl(): State {
        Log.d(TAG, statePlayer.toString())
        when (statePlayer) {
            StateDto.STATE_PLAYING -> {
                pausePlayer()
                return State.STATE_PAUSED
            }

            StateDto.STATE_PREPARED, StateDto.STATE_PAUSED -> {
                startPlayer()
                return State.STATE_PLAYING
            }

            StateDto.STATE_DEFAULT -> throw IllegalStateException("Player is not ready!")
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
            statePlayer = StateDto.STATE_PAUSED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            statePlayer = StateDto.STATE_PREPARED
            onCompletePlay()
        }
    }

    override fun release() {
        mediaPlayer.release()
        statePlayer = StateDto.STATE_DEFAULT
    }

    override val currentPosition: Int
        get() = if (statePlayer != StateDto.STATE_DEFAULT) mediaPlayer.currentPosition else 0


    companion object {
        private final val TAG = "AudioPlayerInteractorImpl"
    }
}