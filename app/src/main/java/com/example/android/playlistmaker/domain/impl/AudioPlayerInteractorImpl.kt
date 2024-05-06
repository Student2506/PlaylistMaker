package com.example.android.playlistmaker.domain.impl

import android.media.MediaPlayer
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.domain.api.AudioPlayerInteractor.STATE
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerInteractorImpl : AudioPlayerInteractor {

    private val mediaPlayer = MediaPlayer()
    private val timeFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }

    override var statePlayer: STATE = STATE.STATE_DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        statePlayer = STATE.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        statePlayer = STATE.STATE_PAUSED
    }

    override fun playbackControl(): Boolean {
        Log.d(TAG, statePlayer.toString())
        when (statePlayer) {
            STATE.STATE_PLAYING -> {
                Log.d(TAG, "State PLAYING: to pause")
                pausePlayer()
                return true
            }
            STATE.STATE_PREPARED, STATE.STATE_PAUSED -> {
                Log.d(TAG,"State PAUSED: to play")
                startPlayer()
                return false
            }
            STATE.STATE_DEFAULT -> throw IllegalStateException("Player is not ready!")
        }
    }

    override fun preparePlayer(trackUrl: String, button: ImageButton, elapsedTime: TextView) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            button.isEnabled = true
            statePlayer = STATE.STATE_PAUSED
        }
        mediaPlayer.setOnCompletionListener {
            button.setImageResource(R.drawable.play)
            statePlayer = STATE.STATE_PREPARED
            elapsedTime.text = timeFormatter.format(0L)
        }
    }

    override fun release() {
        mediaPlayer.release()
    }

    companion object {
        private final val TAG = "AudioPlayerInteractorImpl"
    }
}