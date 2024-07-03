package com.example.android.playlistmaker.player.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android.playlistmaker.player.data.PlayerClient
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.TrackTimeState

class AndroidStandardPlayerClient(private var mediaPlayer: MediaPlayer) : PlayerClient {

    private var currentTrack: String? = null
    private var statePlayer: State = State.Default
    private val handler = Handler(Looper.getMainLooper())

    override fun getTime(): TrackTimeState {
        return TrackTimeState(time = mediaPlayer.currentPosition, state = statePlayer)
    }

    override fun doRequest(dto: Any): State {
        if (dto is Command) {
            when (dto) {
                is Command.Prepare -> preparePlayer(dto.trackUrl)
                is Command.Play -> startPlayer()
                is Command.Pause -> pausePlayer()
                is Command.PlayPause -> playbackControl()
                is Command.Release -> releasePlayer()
            }
            return statePlayer
        } else {
            return State.Default
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
        statePlayer = State.Playing
    }

    private fun pausePlayer() {
        Log.d(TAG, "Not playing")
        if (statePlayer is State.Playing) mediaPlayer.pause()
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
        mediaPlayer.reset()
    }

    private fun trackPlayedLength(): Int =
        if (statePlayer !is State.Default) mediaPlayer.currentPosition else 0


    companion object {
        private val TAG = "AndroidStandardPlayerClient"
        private const val REFRESH_TRACK_DELAY_MILLIS = 400L
    }
}