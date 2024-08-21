package com.example.android.playlistmaker.player.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.util.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: AudioPlayerInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<State>()
    fun observeState(): LiveData<State> = stateLiveData
    private val stateTrackLiveData = SingleLiveEvent<Int>()
    fun observeTrackState(): LiveData<Int> = stateTrackLiveData
    private val stateFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = stateFavoriteLiveData
    private var timerJob: Job? = null
    private val playlistLiveData = MutableLiveData<PlaylistState>()
    fun observePlaylists(): LiveData<PlaylistState> = playlistLiveData

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(REFRESH_TRACK_DELAY_MILLIS)
                playerInteractor.getTrackTime().collect { state ->
                    stateTrackLiveData.postValue(state.progress)
                    renderState(state)
                }
            }
        }
    }

    fun preparePlayer() {
        Log.d(TAG, "Start to prepare")
        viewModelScope.launch {
            playerInteractor.favoriteTrack(track.trackId).collect { is_favorite ->
                stateFavoriteLiveData.postValue(is_favorite)
            }
            playlistInteractor.retrievePlaylists().collect { playlists ->
                if (playlists.isNullOrEmpty()) playlistLiveData.postValue(PlaylistState.Empty)
                else playlistLiveData.postValue(PlaylistState.Content(playlists))
            }
        }

        if (track.previewUrl != null) {
            viewModelScope.launch {
                playerInteractor.controlPlayer(Command.Prepare(track.previewUrl)).collect { state ->
                    renderState(state)
                    Log.d(TAG, "Cooking")
                    stateTrackLiveData.postValue(state.progress)
                }
                Log.d(TAG, "Done prepare")
            }
        }
        startTimer()
    }


    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        releasePlayer()
    }

    private fun startPlayer() {
        viewModelScope.launch {
            playerInteractor.controlPlayer(Command.Play).collect { state ->
                renderState(state)
            }
        }
    }

    fun pausePlayer() {
        viewModelScope.launch {
            playerInteractor.controlPlayer(Command.Pause).collect { state ->
                renderState(state)
            }
        }
    }

    fun releasePlayer() {
        viewModelScope.launch {
            playerInteractor.controlPlayer(Command.Release).collect { state ->
                renderState(state)
            }
        }

    }

    fun playbackControl() {
        viewModelScope.launch {
            playerInteractor.controlPlayer(Command.PlayPause).collect { state ->
                renderState(state)
            }
        }

    }

    fun updateFavorite() {
        viewModelScope.launch {
            playerInteractor.switchFavorite(track).collect { is_favorite ->
                stateFavoriteLiveData.postValue(is_favorite)
            }
        }
    }

    private fun renderState(state: State) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val REFRESH_TRACK_DELAY_MILLIS = 300L  // 2-3 time a second
        private const val TAG = "PlayerController"
    }

}