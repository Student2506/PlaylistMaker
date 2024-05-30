package com.example.android.playlistmaker.player.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    private val track: Track,
    application: Application,
) : AndroidViewModel(application) {

    private val playerInteractor = Creator.provideAudioPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData
    private val stateTrackLiveData = MutableLiveData<Long>()
    fun observeTrackState(): LiveData<Long> = stateTrackLiveData

    private val trackTime: Runnable by lazy {
        object : Runnable {
            override fun run() {
                playerInteractor.getTrackTime(object :
                    AudioPlayerInteractor.AudioPlayerTrackTimeConsumer {
                    override fun getTime(time: Long) {
                        stateTrackLiveData.postValue(time)
                    }
                })
                handler.postDelayed(this, REFRESH_TRACK_DELAY_MILLIS)
            }
        }
    }

    fun preparePlayer() {
        Log.d(TAG, "Prepare Player")
        if (track.previewUrl != null) {
            playerInteractor.controlPlayer(
                Command.Prepare(track.previewUrl),
                object : AudioPlayerInteractor.AudioPlayerConsumer {
                    override fun consume(status: State) {
                        renderState(PlayerState.isLoaded)
                        stateTrackLiveData.postValue(0L)
                    }
                })
        }
    }

    fun onDestroy() {
        releasePlayer()
        handler.removeCallbacks(trackTime)
    }

    private fun startPlayer() {
        playerInteractor.controlPlayer(Command.Play,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    renderState(PlayerState.Content(true))
                    handler.postDelayed(trackTime, REFRESH_TRACK_DELAY_MILLIS)
                }
            })
    }

    fun pausePlayer() {
        playerInteractor.controlPlayer(Command.Pause,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    renderState(PlayerState.Content(true))
                }
            })
        handler.removeCallbacks(trackTime)
    }

    fun releasePlayer() {
        playerInteractor.controlPlayer(Command.Release,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    Log.d(TAG, "Released")
                }
            })
    }

    fun playbackControl() {
        playerInteractor.controlPlayer(Command.PlayPause,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    if (status == State.STATE_PLAYING) {
                        renderState(PlayerState.Content(false))
                    } else {
                        renderState(PlayerState.Content(true))
                    }
                }
            })
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val REFRESH_TRACK_DELAY_MILLIS = 400L  // 2-3 time a second
        private const val TAG = "PlayerController"

        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(
                    track,
                    this[APPLICATION_KEY] as Application
                )
            }
        }
    }

}