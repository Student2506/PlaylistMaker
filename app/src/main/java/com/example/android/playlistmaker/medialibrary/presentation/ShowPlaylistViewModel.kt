package com.example.android.playlistmaker.medialibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.medialibrary.data.converters.TrackConverter
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import kotlinx.coroutines.launch

class ShowPlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val trackConverter: TrackConverter,
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<Playlist>()
    fun observeState(): LiveData<Playlist> = _stateLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.retreivePlaylistById(playlistId = playlistId).collect { playlist ->
                _stateLiveData.postValue(playlist)
            }
        }
    }

    fun tracksToPlaylistTracks(tracks: List<PlaylistTrack>): ArrayList<Track> =
        trackConverter.map(tracks)

    fun requestToRemoveTrackFromPlaylist(playlistId: Long, trackId: Long) {
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(playlistId, trackId)
            playlistInteractor.retreivePlaylistById(playlistId = playlistId).collect { playlist ->
                _stateLiveData.postValue(playlist)
            }
        }
    }
}