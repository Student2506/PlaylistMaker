package com.example.android.playlistmaker.search.presentation

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class TrackSearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    private val tracks = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())
    private var searchQuery: String? = null
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQEUEST_TOKEN)
    }

    fun onStart() {
        setHistoryTracks()
    }

    fun searchDebounce(changedText: String) {

        handler.removeCallbacksAndMessages(SEARCH_REQEUEST_TOKEN)
        val searchRunnable = Runnable { searchSong(changedText) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(
                searchRunnable,
                SEARCH_REQEUEST_TOKEN,
                SEARCH_DEBOUNCE_DELAY
            )
        } else {
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQEUEST_TOKEN,
                postTime
            )
        }
    }

    fun searchSong(songTitle: String) {
        renderState(TracksState.Loading)

        tracksInteractor.searchTracks(songTitle, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
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
        })

    }


    fun showTrack(track: Track) {
        val message = "${track.trackName} - ${track.artistName}\nTime:${track.trackTime}"
        Log.d(TAG, message)
        val historyTracks = arrayListOf<Track>()
        sharedPreferencesInteractor.getFilmsHistory(object :
            SharedPreferencesInteractor.SharedPreferencesConsumer {
            override fun consume(result: Any) {
                if (result is String && result != "")
                    historyTracks.addAll(
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
                sharedPreferencesInteractor.putFilmsHistory(historyTracksJson,
                    object : SharedPreferencesInteractor.SharedPreferencesConsumer {
                        override fun consume(result: Any) {
                            setHistoryTracks()
                        }
                    })

            }
        })


    }

    fun setHistoryTracks() {
        val historyTracks = arrayListOf<Track>()
        sharedPreferencesInteractor.getFilmsHistory(object :
            SharedPreferencesInteractor.SharedPreferencesConsumer {
            override fun consume(result: Any) {
                if (result is String && result != "")
                    historyTracks.addAll(
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
        private val SEARCH_REQEUEST_TOKEN = Any()
        private const val MAXIMUM_HISTORY_LENGTH = 10
        private const val TAG = "SearchController"

    }
}