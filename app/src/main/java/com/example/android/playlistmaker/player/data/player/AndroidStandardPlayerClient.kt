package com.example.android.playlistmaker.player.data.player

import android.media.MediaPlayer
import com.example.android.playlistmaker.player.data.PlayerClient
import com.example.android.playlistmaker.player.data.dto.CommandDto
import com.example.android.playlistmaker.player.data.dto.PlayerRequest
import com.example.android.playlistmaker.player.data.dto.PlayerResponse
import com.example.android.playlistmaker.player.data.dto.PlayerTimeRequest
import com.example.android.playlistmaker.player.data.dto.PlayerTimeResponse
import com.example.android.playlistmaker.player.data.dto.Response
import com.example.android.playlistmaker.player.data.dto.StateDto

class AndroidStandardPlayerClient : PlayerClient {

    private val mediaPlayer = MediaPlayer()
    private var currentTrack: String? = null
    private var statePlayer: StateDto = StateDto.Default

    override fun doRequest(dto: Any): Response {
        if (dto is PlayerRequest) {
            when (dto.command) {
                is CommandDto.Prepare -> preparePlayer(dto.command.trackUrl)
                is CommandDto.Play -> startPlayer()
                is CommandDto.Pause -> pausePlayer()
                is CommandDto.PlayPause -> playbackControl()
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
        if (currentTrack != trackUrl) {
            currentTrack = trackUrl
            mediaPlayer.reset()
        }
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            statePlayer = StateDto.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            statePlayer = StateDto.Prepared
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        statePlayer = StateDto.Playing
    }

    private fun pausePlayer() {
        if (statePlayer is StateDto.Playing) mediaPlayer.pause()
        statePlayer = StateDto.Paused
    }

    private fun playbackControl() {
        when (statePlayer) {
            is StateDto.Playing -> {
                pausePlayer()
            }

            is StateDto.Prepared, StateDto.Paused -> {
                startPlayer()
            }

            is StateDto.Default -> throw IllegalStateException("Player is not ready!")
        }
    }

    private fun trackPlayedLength(): Int =
        if (statePlayer !is StateDto.Default) mediaPlayer.currentPosition else 0


    companion object {
        private final val TAG = "AndroidStandardPlayerClient"
    }
}