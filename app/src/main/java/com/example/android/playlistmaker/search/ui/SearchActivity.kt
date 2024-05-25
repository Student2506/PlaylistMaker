package com.example.android.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.main.CustomApp
import com.example.android.playlistmaker.main.CustomApp.Companion.KEY_FOR_SEARCH_HISTORY
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {


    private var searchQuery: String = ""
    private val baseUrl: String = "https://itunes.apple.com"


    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            showTrack(it)
        }
    }

    private var recycler: RecyclerView? = null
    private var nothingFound: TextView? = null
    private var refreshButton: Button? = null
    private var noConnection: TextView? = null
    private var lastRequest: String? = null
    private var progressBar: ProgressBar? = null

    private val historyTracks: ArrayList<Track> = arrayListOf()
    private var sharedPrefs: SharedPreferences? = null

    private var clearHistory: MaterialButton? = null
    private var tvHistoryHeader: TextView? = null

    private val inputEditText: EditText by lazy { findViewById(R.id.etInput) }
    private val searchRunnable = Runnable {
        if (inputEditText.text.isNotEmpty()) {
            searchSong(inputEditText.text.toString())
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val tracksInteractor = Creator.provideTracksInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val clearButton = findViewById<ImageView>(R.id.ivClear)
        val headerButton = findViewById<FrameLayout>(R.id.flBackToMain)
        headerButton.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            val size = tracks.size
            tracks.clear()
            adapter.tracks = historyTracks
            if (adapter.tracks.isNotEmpty()) {
                tvHistoryHeader?.isVisible = true
                clearHistory?.isVisible = true
            }
            adapter.notifyItemRangeChanged(0, size)
        }
        inputEditText.setText(searchQuery)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    tvHistoryHeader?.isVisible = false
                    clearHistory?.isVisible = false
                    searchSong(inputEditText.text.toString())
                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                true
            } else {
                false
            }
        }
        inputEditText.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                clearButton.visibility = getClearButtonVisibility(charSequence)
                searchDebounce()
            },
            afterTextChanged = { _ ->
                searchQuery = inputEditText.text.toString()
            }
        )
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            tvHistoryHeader?.isVisible = false
            clearHistory?.isVisible = false

            if (hasFocus && inputEditText.text.isEmpty()) {
                val size = adapter.tracks.size
                adapter.tracks = arrayListOf()
                adapter.notifyItemRangeRemoved(0, size)
            } else {
                adapter.tracks = tracks
                adapter.notifyItemRangeChanged(0, tracks.size)
            }
        }

        recycler = findViewById(R.id.rvTracks)
        nothingFound = findViewById(R.id.tvNothingFound)
        refreshButton = findViewById(R.id.btRefresh)
        noConnection = findViewById(R.id.tvNoSignal)
        progressBar = findViewById(R.id.pbProgressBar)
        adapter.tracks = tracks
        recycler?.layoutManager = LinearLayoutManager(this)
        recycler?.adapter = adapter

        refreshButton?.setOnClickListener {
            searchSong(lastRequest!!)
        }
        clearHistory = findViewById<MaterialButton?>(R.id.btClearHistory).also {
            it.isVisible = false
        }
        tvHistoryHeader = findViewById<TextView?>(R.id.tvHistoryHeader).also {
            it.isVisible = false
        }
        clearHistory?.setOnClickListener {
            val size = historyTracks.size
            historyTracks.clear()
            adapter.notifyItemRangeRemoved(0, size)
            tvHistoryHeader?.isVisible = false
            clearHistory?.isVisible = false
        }

        sharedPrefs = getSharedPreferences(CustomApp.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
    }

    override fun onStop() {
        super.onStop()
        val historyTracksJson = Gson().toJson(historyTracks)
        sharedPrefs!!.edit { putString(KEY_FOR_SEARCH_HISTORY, historyTracksJson) }
    }

    override fun onStart() {
        super.onStart()
        val historyTracksJson = sharedPrefs?.getString(KEY_FOR_SEARCH_HISTORY, null)
        if (historyTracksJson.isNullOrEmpty()) return
        historyTracks.clear()
        historyTracks.addAll(Gson().fromJson(historyTracksJson, Array<Track>::class.java))
        adapter.tracks = historyTracks
        if (adapter.tracks.isNotEmpty()) {
            tvHistoryHeader?.isVisible = true
            clearHistory?.isVisible = true
        } else {
            tvHistoryHeader?.isVisible = false
            clearHistory?.isVisible = false
        }
        Log.d(TAG, "History tracks here?")
        adapter.notifyDataSetChanged()
        Log.d(TAG, "IT'S HERE")
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY, DEFAULT_QUERY)
    }

    private fun getClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchSong(songTitle: String) {
        recycler?.isVisible = false
        noConnection?.isVisible = false
        nothingFound?.isVisible = false
        refreshButton?.isVisible = false
        clearHistory?.isVisible = false
        tvHistoryHeader?.isVisible = false
        progressBar?.isVisible = true
        Log.d(TAG, "Progress Bar UP")

//        itunesService.searchTracks(songTitle).enqueue(object : Callback<ITunesTrackResponse> {
//            override fun onResponse(
//                call: Call<ITunesTrackResponse>, response: Response<ITunesTrackResponse>
//            ) {
//                adapter.tracks = tracks
//                progressBar?.isVisible = false
//                Log.d(TAG, "Progress Bar DOWN")
//                when (response.code()) {
//                    HTTP_OK -> {
//                        refreshButton?.isVisible = false
//                        noConnection?.isVisible = false
//                        if (response.body()?.tracks?.isNotEmpty() == true) {
//                            nothingFound?.isVisible = false
//                            tracks.clear()
//                            tracks.addAll(response.body()?.tracks!!)
//                            recycler?.isVisible = true
//                            adapter.notifyItemRangeChanged(0, tracks.size)
//                        } else {
//                            val size = tracks.size
//                            tracks.clear()
//                            adapter.notifyItemRangeRemoved(0, size)
//                            recycler?.isVisible = false
//                            nothingFound?.isVisible = true
//                        }
//                    }
//
//                    else -> {
//                        lastRequest = songTitle
//                        showErrorMessage()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ITunesTrackResponse>, t: Throwable) {
//                lastRequest = songTitle
//                showErrorMessage()
//            }
//        })
        tracksInteractor.searchTracks(songTitle, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                tracks.clear()
                tracks.addAll(foundTracks)
                adapter.tracks = tracks
                handler.post(object : Runnable {
                    override fun run() {
                        adapter.notifyItemRangeChanged(0, adapter.tracks.size)
                        recycler?.isVisible = true
                        progressBar?.isVisible = false

                    }
                })
            }
        })

    }

    private fun showErrorMessage() {
        val size = tracks.size
        tracks.clear()
        adapter.notifyItemRangeRemoved(0, size)
        recycler?.isVisible = false
        nothingFound?.isVisible = false
        noConnection?.isVisible = true
        refreshButton?.isVisible = true
    }

    private fun showTrack(track: Track) {
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
        Log.d(TAG, historyTracks.toString())
        adapter.notifyItemChanged(0)
        val settingsIntent = Intent(this, AudioPlayerActivity::class.java)
        settingsIntent.putExtra(TRACK_TO_SHOW, track)
        startActivity(settingsIntent)

    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val DEFAULT_QUERY = ""
        const val TRACK_TO_SHOW = "track_to_show"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val TAG = "SearchActivity"
    }
}