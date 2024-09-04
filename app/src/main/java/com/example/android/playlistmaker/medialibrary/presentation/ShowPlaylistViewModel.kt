package com.example.android.playlistmaker.medialibrary.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.medialibrary.data.converters.TrackConverter
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ShowPlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val trackConverter: TrackConverter,
    private val application: Application,
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ShowPlaylistViewModel"
    }

    private val _stateLiveData = MutableLiveData<Playlist>()
    fun observeState(): LiveData<Playlist> = _stateLiveData
    private val _stateTracksLiveData = MutableLiveData<List<PlaylistTrack>>()
    fun observeOrderedTracks(): LiveData<List<PlaylistTrack>> = _stateTracksLiveData
    private val _stateTracksQty = MutableLiveData<String>()
    fun observeTrackQty(): LiveData<String> = _stateTracksQty
    private val _statePlaylistDuration = MutableLiveData<String>()
    fun observePlaylistDuration(): LiveData<String> = _statePlaylistDuration

    init {
        viewModelScope.launch {
            playlistInteractor.retrieveTracksOrdered(playlistId = playlistId).collect { tracks ->
                _stateTracksLiveData.postValue(tracks)
                playlistInteractor.retreivePlaylistById(playlistId = playlistId)
                    .collect { playlist ->
                        _stateLiveData.postValue(playlist)
                        _stateTracksQty.postValue(
                            application.resources.getQuantityString(
                                R.plurals.tracks_plural,
                                playlist.tracks?.size ?: 0,
                                playlist.tracks?.size ?: 0
                            )
                        )
                        if (playlist.tracks != null) {
                            var totalLength = 0L
                            for (track in playlist.tracks) {
                                totalLength += track.trackTime
                            }
                            val total = totalLength / 60000
                            _statePlaylistDuration.postValue(
                                application.resources.getQuantityString(
                                    R.plurals.minutes_plural, total.toInt(), total
                                )
                            )
                        } else {
                            _statePlaylistDuration.postValue(
                                application.resources.getQuantityString(
                                    R.plurals.minutes_plural, 0, 0
                                )
                            )
                        }
                    }
            }
        }
    }

    fun tracksToPlaylistTracks(tracks: List<PlaylistTrack>): ArrayList<Track> =
        trackConverter.map(tracks)

    fun requestToRemoveTrackFromPlaylist(playlistId: Long, trackId: Long) {
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(playlistId, trackId)
            playlistInteractor.retrieveTracksOrdered(playlistId = playlistId).collect { tracks ->
                _stateTracksLiveData.postValue(tracks)
                playlistInteractor.retreivePlaylistById(playlistId = playlistId)
                    .collect { playlist ->
                        _stateLiveData.postValue(playlist)
                    }
            }
        }
    }

    fun removePlaylist(playlistId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "Start remove playlist")
            playlistInteractor.removePlaylist(playlistId)
            playlistInteractor.retreivePlaylistById(playlistId = playlistId).collect { playlist ->
                _stateLiveData.postValue(playlist)
            }
        }
    }

    fun buildMessage(): String {
        val playlist = _stateLiveData.value
        val sb = StringBuilder()
        val timeFormatter: SimpleDateFormat = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        )
        sb.append("Название: ${playlist?.title}\n")
        if (!playlist?.description.isNullOrBlank()) {
            sb.append("Описание: ${playlist?.description}\n")
        }
        sb.append(
            application.resources.getQuantityString(
                R.plurals.tracks_plural, playlist?.tracks?.size ?: 0, playlist?.tracks?.size ?: 0
            )
        )
        playlist?.tracks?.forEachIndexed { i, elem ->
            sb.append("${i + 1}.${elem.artistName} - ${elem.trackName} (${timeFormatter.format(elem.trackTime)})\n")
        }
        return sb.toString()
    }
}