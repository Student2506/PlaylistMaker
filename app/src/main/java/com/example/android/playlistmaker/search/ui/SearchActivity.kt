package com.example.android.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.example.android.playlistmaker.search.domain.models.Track

class SearchActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            showTrack(it)
        }
    }
    private val searchController = Creator.provideSearchController(this, adapter)

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchController.onCreate()
    }

    override fun onStop() {
        super.onStop()
        searchController.onStop()
    }

    override fun onStart() {
        super.onStart()
        searchController.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        searchController.onDestroy()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showTrack(track: Track) {
        searchController.showTrack(track)
        val settingsIntent = Intent(this, AudioPlayerActivity::class.java)
        settingsIntent.putExtra(SearchActivity.TRACK_TO_SHOW, track)
        startActivity(settingsIntent)
    }

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val DEFAULT_QUERY = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val TRACK_TO_SHOW = "track_to_show"
        private const val TAG = "SearchActivity"
    }
}