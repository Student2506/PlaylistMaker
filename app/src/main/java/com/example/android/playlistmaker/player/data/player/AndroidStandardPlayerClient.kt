package com.example.android.playlistmaker.player.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android.playlistmaker.player.data.PlayerClient
import com.example.android.playlistmaker.player.data.dto.PlayerRequest
import com.example.android.playlistmaker.player.data.dto.PlayerResponse
import com.example.android.playlistmaker.player.data.dto.PlayerTimeRequest
import com.example.android.playlistmaker.player.data.dto.PlayerTimeResponse
import com.example.android.playlistmaker.player.data.dto.Response
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

class AndroidStandardPlayerClient : PlayerClient {

    private var mediaPlayer: MediaPlayer? = null
    private var currentTrack: String? = null
    private var statePlayer: State = State.Default
    private val handler = Handler(Looper.getMainLooper())

    override fun getTime(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun doRequest(dto: Any): Response {
        if (dto is PlayerRequest) {
            when (dto.command) {
                is Command.Prepare -> preparePlayer(dto.command.trackUrl)
                is Command.Play -> startPlayer()
                is Command.Pause -> pausePlayer()
                is Command.PlayPause -> playbackControl()
                is Command.Release -> releasePlayer()
            }
            return PlayerResponse(statePlayer).apply { resultCode = 0 }
        } else if (dto is PlayerTimeRequest) {
            return PlayerTimeResponse(trackPlayedLength())
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

    private fun preparePlayer(
        trackUrl: String,
    ) {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer()
        if (currentTrack != trackUrl) {
            currentTrack = trackUrl
            mediaPlayer?.reset()
        }
        mediaPlayer?.setDataSource(trackUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            statePlayer = State.Prepared
        }
        mediaPlayer?.setOnCompletionListener {
            statePlayer = State.Prepared
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        Log.d(TAG, "Playing")
        statePlayer = State.Playing
    }

    private fun pausePlayer() {
        Log.d(TAG, "Not playing")
        if (statePlayer is State.Playing) mediaPlayer?.pause()
        statePlayer = State.Paused
    }

    private fun playbackControl() {
        when (statePlayer) {
            is State.Playing -> {
                pausePlayer()
            }

            is State.Prepared, State.Paused -> {
                startPlayer()
            }

            is State.Default -> throw IllegalStateException("Player is not ready!")
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun trackPlayedLength(): Int =
        if (statePlayer !is State.Default) mediaPlayer?.currentPosition!! else 0


    companion object {
        private final val TAG = "AndroidStandardPlayerClient"
        private const val REFRESH_TRACK_DELAY_MILLIS = 400L
    }
}