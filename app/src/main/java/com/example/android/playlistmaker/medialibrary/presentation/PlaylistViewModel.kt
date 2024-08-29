package com.example.android.playlistmaker.medialibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = _stateLiveData

    fun onStart() {
        setPlaylists()
    }

    fun setPlaylists() {
        viewModelScope.launch {
            playlistInteractor.retreivePlaylists().collect { playlists ->
                if (playlists.isNullOrEmpty()) _stateLiveData.postValue(PlaylistState.Empty)
                else _stateLiveData.postValue(PlaylistState.Content(playlists))
            }
        }
    }
}