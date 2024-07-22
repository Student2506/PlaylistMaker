package com.example.android.playlistmaker.player.data.player

import android.media.MediaPlayer
import android.util.Log
import com.example.android.playlistmaker.player.data.PlayerClient
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidStandardPlayerClient(private var mediaPlayer: MediaPlayer) : PlayerClient {

    private var currentTrack: String? = null
    private var statePlayer: State = State.Default

    override suspend fun getTime(): State {
        if (statePlayer is State.Playing) statePlayer = State.Playing(mediaPlayer.currentPosition)
        if (statePlayer is State.Paused) statePlayer = State.Paused(mediaPlayer.currentPosition)
        return statePlayer
    }

    override suspend fun doRequest(dto: Any): State {
        return withContext(Dispatchers.Unconfined) {
            if (dto !is Command) {
                State.Default
            }
            when (dto) {
                is Command.Prepare -> preparePlayer(dto.trackUrl)
                is Command.Play -> startPlayer()
                is Command.Pause -> pausePlayer()
                is Command.PlayPause -> playbackControl()
                is Command.Release -> releasePlayer()
            }
            statePlayer
        }
    }

    private fun preparePlayer(
        trackUrl: String,
    ) {
        currentTrack = trackUrl
        mediaPlayer.reset()
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            statePlayer = State.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            statePlayer = State.Prepared
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        Log.d(TAG, "Playing")
        statePlayer = State.Playing(mediaPlayer.currentPosition)
    }

    private fun pausePlayer() {
        Log.d(TAG, "Not playing")
        if (statePlayer is State.Playing) mediaPlayer.pause()
        statePlayer = State.Paused(mediaPlayer.currentPosition)
    }

    private fun playbackControl() {
        when (statePlayer) {
            is State.Playing -> {
                pausePlayer()
            }

            is State.Prepared -> {
                startPlayer()
            }

            is State.Paused -> {
                startPlayer()
            }

            is State.Default -> throw IllegalStateException("Player is not ready!")
        }
    }

    private fun releasePlayer() {
        mediaPlayer.reset()
    }


    companion object {
        private val TAG = "AndroidStandardPlayerClient"
    }
}