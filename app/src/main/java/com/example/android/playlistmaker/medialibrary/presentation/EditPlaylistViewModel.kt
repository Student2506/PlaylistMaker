package com.example.android.playlistmaker.medialibrary.presentation

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    application: Application,
    private val playlistInteractor: PlaylistInteractor,
) : CreatePlaylistViewModel(application, playlistInteractor) {

    private val _playlist = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = _playlist

    private var currentUri: Uri? = null

    init {
        viewModelScope.launch {
            playlistInteractor.retreivePlaylistById(playlistId).collect {
                _playlist.postValue(it)
            }
        }
    }
    private companion object {
        const val TAG = "EditPlaylistViewModel"
    }

    fun savePlaylist(title: String, description: String?, cover: String?) {
        currentUri = super.saveImageToPrivateStorage(title, cover)
        viewModelScope.launch {
            playlistInteractor.createPlaylist(
                Playlist(
                    id = playlistId,
                    title = title,
                    description = description ?: _playlist.value?.description,
                    tracks = emptyList<PlaylistTrack>(),
                    imageUrl = currentUri.toString()
                )
            )
        }
        toastLiveData.postValue(title)
    }
}