package com.example.android.playlistmaker.player.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<State>()
    fun observeState(): LiveData<State> = stateLiveData
    private val stateTrackLiveData = SingleLiveEvent<Int>()
    fun observeTrackState(): LiveData<Int> = stateTrackLiveData
    private val stateFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = stateFavoriteLiveData
    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(REFRESH_TRACK_DELAY_MILLIS)
                playerInteractor.getTrackTime(object :
                    AudioPlayerInteractor.AudioPlayerTrackTimeConsumer {
                    override fun getTime(state: State) {
                        stateTrackLiveData.postValue(state.progress)
                        renderState(state)
                    }
                })
            }
        }
    }

    fun preparePlayer() {
        if (track.previewUrl != null) {
            playerInteractor.controlPlayer(Command.Prepare(track.previewUrl),
                object : AudioPlayerInteractor.AudioPlayerConsumer {
                    override fun consume(status: State) {
                        renderState(State.Prepared)
                        stateTrackLiveData.postValue(0)
                    }
                })
        }
        startTimer()
    }


    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        releasePlayer()
    }

    private fun startPlayer() {
        playerInteractor.controlPlayer(
            Command.Play,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {}
            })
    }

    fun pausePlayer() {
        playerInteractor.controlPlayer(
            Command.Pause,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {}
            })
    }

    fun releasePlayer() {
        playerInteractor.controlPlayer(
            Command.Release,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    Log.d(TAG, "Released")
                }
            })
    }

    fun playbackControl() {
        playerInteractor.controlPlayer(
            Command.PlayPause,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {}
            })
    }

    fun updateFavorite() {
        track.isFavorite = !track.isFavorite
        stateFavoriteLiveData.postValue(track.isFavorite)
    }

    private fun renderState(state: State) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val REFRESH_TRACK_DELAY_MILLIS = 300L  // 2-3 time a second
        private const val TAG = "PlayerController"
    }

}