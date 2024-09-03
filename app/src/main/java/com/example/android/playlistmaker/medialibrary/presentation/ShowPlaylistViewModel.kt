package com.example.android.playlistmaker.medialibrary.presentation

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Locale

class ShowPlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val trackConverter: TrackConverter,
) : ViewModel() {

    companion object {
        private const val TAG = "ShowPlaylistViewModel"
    }

    private val _stateLiveData = MutableLiveData<Playlist>()
    fun observeState(): LiveData<Playlist> = _stateLiveData
    private val _stateTracksLiveData = MutableLiveData<List<PlaylistTrack>>()
    fun observeOrderedTracks(): LiveData<List<PlaylistTrack>> = _stateTracksLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.retrieveTracksOrdered(playlistId = playlistId).collect { tracks ->
                _stateTracksLiveData.postValue(tracks)
                playlistInteractor.retreivePlaylistById(playlistId = playlistId)
                    .collect { playlist ->
                        _stateLiveData.postValue(playlist)
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
        sb.append("${TrackCount(playlist?.tracks?.size, true)}\n")
        playlist?.tracks?.forEachIndexed { i, elem ->
            sb.append("${i + 1}.${elem.artistName} - ${elem.trackName} (${timeFormatter.format(elem.trackTime)})\n")
        }
        return sb.toString()
    }

    fun TrackCount(trackQty: Int?, isTwoDigit: Boolean = false): String {
        if (trackQty == null || trackQty == 0) return "0 треков"
        var number = trackQty.toString()
        if (isTwoDigit) {
            val f: NumberFormat = DecimalFormat("00")
            number = f.format(trackQty)
        }
        if (trackQty % 100 in 5..20) return "$number треков"
        if (trackQty % 10 == 1) return "$number трек"
        if (trackQty % 10 in 2..4) return "$number трека"
        else return "$trackQty треков"
    }
}