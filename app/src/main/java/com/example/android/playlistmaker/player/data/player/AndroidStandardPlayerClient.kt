package com.example.android.playlistmaker.player.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android.playlistmaker.player.data.PlayerClient
import com.example.android.playlistmaker.player.data.dto.CommandDto
import com.example.android.playlistmaker.player.data.dto.PlayerRequest
import com.example.android.playlistmaker.player.data.dto.PlayerResponse
import com.example.android.playlistmaker.player.data.dto.PlayerTimeRequest
import com.example.android.playlistmaker.player.data.dto.PlayerTimeResponse
import com.example.android.playlistmaker.player.data.dto.Response
import com.example.android.playlistmaker.player.data.dto.StateDto

class AndroidStandardPlayerClient : PlayerClient {

    private var mediaPlayer: MediaPlayer? = null
    private var currentTrack: String? = null
    private var statePlayer: StateDto = StateDto.Default
    private val handler = Handler(Looper.getMainLooper())
    private var currentTime = 0
    private val trackTime: Runnable by lazy {
        object : Runnable {
            override fun run() {
                currentTime = mediaPlayer?.currentPosition ?: 0
                handler.postDelayed(
                    this,
                    REFRESH_TRACK_DELAY_MILLIS
                )
            }
        }
    }

    override fun getTime(): Int {
        return currentTime
    }

    override fun doRequest(dto: Any): Response {
        if (dto is PlayerRequest) {
            when (dto.command) {
                is CommandDto.Prepare -> preparePlayer(dto.command.trackUrl)
                is CommandDto.Play -> startPlayer()
                is CommandDto.Pause -> pausePlayer()
                is CommandDto.PlayPause -> playbackControl()
                is CommandDto.Release -> releasePlayer()
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
            handler.removeCallbacks(trackTime)
        }
        mediaPlayer?.setDataSource(trackUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            statePlayer = StateDto.Prepared
        }
        mediaPlayer?.setOnCompletionListener {
            statePlayer = StateDto.Prepared
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        Log.d(TAG, "Playing")
        statePlayer = StateDto.Playing
    }

    private fun pausePlayer() {
        Log.d(TAG, "Not playing")
        if (statePlayer is StateDto.Playing) mediaPlayer?.pause()
        statePlayer = StateDto.Paused
    }

    private fun playbackControl() {
        when (statePlayer) {
            is StateDto.Playing -> {
                pausePlayer()
                handler.removeCallbacks(trackTime)
            }

            is StateDto.Prepared, StateDto.Paused -> {
                startPlayer()
                handler.post(trackTime)
            }

            is StateDto.Default -> throw IllegalStateException("Player is not ready!")
        }
    }

    private fun releasePlayer() {
        handler.removeCallbacks(trackTime)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun trackPlayedLength(): Int =
        if (statePlayer !is StateDto.Default) mediaPlayer?.currentPosition!! else 0


    companion object {
        private final val TAG = "AndroidStandardPlayerClient"
        private const val REFRESH_TRACK_DELAY_MILLIS = 400L
    }
}