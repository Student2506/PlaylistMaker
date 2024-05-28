package com.example.android.playlistmaker.search.presentation

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class TrackSearchPresenter(
    private val view: TrackSearchView,
    private val context: Context,
) {
    private val sharedPreferencesInteractor = Creator.provideSharedPreferncesInteractor(context)
    private val tracksInteractor = Creator.provideTracksInteractor(context)

    private val historyTracks: ArrayList<Track> = arrayListOf()
    private val tracks = ArrayList<Track>()


    private val handler = Handler(Looper.getMainLooper())
    private var searchQuery: String? = null

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQEUEST_TOKEN)
    }

    fun onStop() {
        val historyTracksJson = Gson().toJson(historyTracks)
        sharedPreferencesInteractor.putFilmsHistory(historyTracksJson,
            object : SharedPreferencesInteractor.SharedPreferencesConsumer {
                override fun consume(result: Any) {
                    if (result is String) {
                        Log.d(TAG, result)
                    }
                }
            })
    }

    fun onStart() {
        sharedPreferencesInteractor.getFilmsHistory(object :
            SharedPreferencesInteractor.SharedPreferencesConsumer {
            override fun consume(result: Any) {
                if (result is String && result != "") {
                    val historyTracksJson = result
                    historyTracks.clear()
                    historyTracks.addAll(
                        Gson().fromJson(
                            historyTracksJson, Array<Track>::class.java
                        )
                    )
                    handler.post {
//                        view.updateTracksList(historyTracks)
//                        view.showHistoryList(historyTracks.isNotEmpty())
                        view.showContent(historyTracks, true)
                    }
                }
            }
        })
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
        view.showLoading()

        tracksInteractor.searchTracks(songTitle, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                handler.post {
                    if (foundTracks != null) {
                        tracks.clear()
                        tracks.addAll(foundTracks)

                    }
                    when {
                        errorMessage != null -> view.showError()
                        tracks.isEmpty() -> view.showEmpty()
                        else -> view.showContent(tracks)
                    }
                }
            }
        })

    }


    fun showTrack(track: Track) {
        val message = "${track.trackName} - ${track.artistName}\nTime:${track.trackTime}"
        Log.d(TAG, message)
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
        if (historyTracks.size > 10) {
            historyTracks.removeLast()
        }
        view.showContent(historyTracks, true)
    }

    fun setHistoryTracks() {
        view.showContent(historyTracks, true)
    }

    fun setSearchTracks() {
        view.showContent(tracks)
    }

    fun clearHistory() {
        historyTracks.clear()
        view.showContent(historyTracks, true)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQEUEST_TOKEN = Any()
        private const val TAG = "SearchController"
    }
}