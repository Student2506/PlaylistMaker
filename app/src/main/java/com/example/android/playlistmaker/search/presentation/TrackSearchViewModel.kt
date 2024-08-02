package com.example.android.playlistmaker.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.util.debounce
import com.google.gson.Gson
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyTracksInteractor: HistoryTracksInteractor,
) : ViewModel() {

    private val tracks = ArrayList<Track>()

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchSong(changedText)
        }
    private var latestSearchText = ""

    fun onStart() {
        setHistoryTracks()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun searchSong(songTitle: String) {
        if (songTitle.equals("")) return
        renderState(TracksState.Loading)

        viewModelScope.launch {
            tracksInteractor.searchTracks(songTitle).collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        if (foundTracks != null) {
            tracks.clear()
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage != null -> renderState(TracksState.ServerError)
            tracks.isEmpty() -> renderState(TracksState.Empty)
            else -> renderState(TracksState.Content(tracks))
        }
    }

    fun showTrack(track: Track, isFromHistory: Boolean) {
        val message = "${track.trackName} - ${track.artistName}\nTime:${track.trackTime}"
        Log.d(TAG, message)
        val historyTracks = arrayListOf<Track>()
        historyTracksInteractor.getHistoryTracks(object :
            HistoryTracksInteractor.HistoryTracksConsumer {
            override fun consume(result: Any) {
                if (result is String && result != "") historyTracks.addAll(
                    Gson().fromJson(
                        result, Array<Track>::class.java
                    )
                )
                val historyIterator = historyTracks.iterator()
                var counter = 0
                while (historyIterator.hasNext()) {
                    if (track.trackId == historyIterator.next().trackId) {
                        historyIterator.remove()
                        break
                    }
                    counter++
                }
                historyTracks.add(0, track)
                if (historyTracks.size > MAXIMUM_HISTORY_LENGTH) {
                    historyTracks.removeLast()
                }
                val historyTracksJson = Gson().toJson(historyTracks)

                historyTracksInteractor.putHistoryTracks(historyTracksJson,
                    object : HistoryTracksInteractor.HistoryTracksConsumer {
                        override fun consume(result: Any) {
                            if (isFromHistory) {
                                setHistoryTracks()
                            }
                        }
                    })
            }
        })
    }

    fun setHistoryTracks() {
        val historyTracks = arrayListOf<Track>()
        historyTracksInteractor.getHistoryTracks(object :
            HistoryTracksInteractor.HistoryTracksConsumer {
            override fun consume(result: Any) {
                if (result is String && result != "") historyTracks.addAll(
                    Gson().fromJson(
                        result, Array<Track>::class.java
                    )
                )
                renderState(TracksState.HistoryContent(historyTracks))
            }
        })
    }

    fun setSearchTracks() {
        renderState(TracksState.Content(tracks))
    }

    fun clearHistory() {
        renderState(TracksState.HistoryContent(arrayListOf<Track>()))
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val MAXIMUM_HISTORY_LENGTH = 10
        private const val TAG = "SearchController"
    }
}