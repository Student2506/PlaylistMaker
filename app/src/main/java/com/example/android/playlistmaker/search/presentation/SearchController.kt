package com.example.android.playlistmaker.search.presentation

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

class SearchController(
    private val activity: Activity,
    private val adapter: TrackAdapter,
) {
    private var sharedPreferencesInteractor: SharedPreferencesInteractor? = null
    private val tracksInteractor = Creator.provideTracksInteractor(activity)

    private var recycler: RecyclerView? = null
    private var nothingFound: TextView? = null
    private var refreshButton: Button? = null
    private var noConnection: TextView? = null
    private var lastRequest: String? = null
    private var progressBar: ProgressBar? = null
    private var clearHistory: MaterialButton? = null
    private var tvHistoryHeader: TextView? = null

    private val historyTracks: ArrayList<Track> = arrayListOf()
    private val tracks = ArrayList<Track>()

    private val searchRunnable = Runnable {
        if (inputEditText?.text?.isNotEmpty() ?: false) {
            searchSong(inputEditText?.text.toString())
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private var inputEditText: EditText? = null
    private var clearButton: ImageView? = null
    private var headerButton: FrameLayout? = null

    private var searchQuery: String = ""

    fun onCreate() {
        adapter.tracks = tracks
        inputEditText = activity.findViewById(R.id.etInput)
        sharedPreferencesInteractor =
            Creator.provideSharedPreferncesInteractor(activity)
        clearButton = activity.findViewById<ImageView>(R.id.ivClear)
        headerButton = activity.findViewById<FrameLayout>(R.id.flBackToMain)
        headerButton?.setOnClickListener {
            activity.finish()
        }
        clearButton?.setOnClickListener {
            inputEditText?.setText("")
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText?.windowToken, 0)
            val size = tracks.size
            tracks.clear()
            adapter.tracks = historyTracks
            if (adapter.tracks.isNotEmpty()) {
                tvHistoryHeader?.isVisible = true
                clearHistory?.isVisible = true
            }
            adapter.notifyItemRangeChanged(0, size)
        }
        inputEditText?.setText(searchQuery)
        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText?.text?.isNotEmpty() ?: false) {
                    tvHistoryHeader?.isVisible = false
                    clearHistory?.isVisible = false
                    searchSong(inputEditText?.text.toString())
                }
                val inputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(inputEditText?.windowToken, 0)
                true
            } else {
                false
            }
        }
        inputEditText?.addTextChangedListener(beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                clearButton?.isVisible = !charSequence.isNullOrEmpty()
                searchDebounce()
            },
            afterTextChanged = { _ ->
                searchQuery = inputEditText?.text.toString()
            })
        inputEditText?.setOnFocusChangeListener { _, hasFocus ->
            tvHistoryHeader?.isVisible = false
            clearHistory?.isVisible = false

            if (hasFocus && inputEditText?.text?.isEmpty() ?: false) {
                val size = adapter.tracks.size
                adapter.tracks = arrayListOf()
                adapter.notifyItemRangeRemoved(0, size)
            } else {
                adapter.tracks = tracks
                adapter.notifyItemRangeChanged(0, tracks.size)
            }
        }

        recycler = activity.findViewById(R.id.rvTracks)
        nothingFound = activity.findViewById(R.id.tvNothingFound)
        refreshButton = activity.findViewById(R.id.btRefresh)
        noConnection = activity.findViewById(R.id.tvNoSignal)
        progressBar = activity.findViewById(R.id.pbProgressBar)
        recycler?.layoutManager = LinearLayoutManager(activity)
        recycler?.adapter = adapter

        refreshButton?.setOnClickListener {
            searchSong(lastRequest!!)
        }
        clearHistory = activity.findViewById<MaterialButton?>(R.id.btClearHistory).also {
            it.isVisible = false
        }
        tvHistoryHeader = activity.findViewById<TextView?>(R.id.tvHistoryHeader).also {
            it.isVisible = false
        }
        clearHistory?.setOnClickListener {
            val size = historyTracks.size
            historyTracks.clear()
            adapter.notifyItemRangeRemoved(0, size)
            tvHistoryHeader?.isVisible = false
            clearHistory?.isVisible = false
        }
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun onStop() {
        val historyTracksJson = Gson().toJson(historyTracks)
        sharedPreferencesInteractor?.putFilmsHistory(historyTracksJson,
            object : SharedPreferencesInteractor.SharedPreferencesConsumer {
                override fun consume(result: Any) {
                    if (result is String) {
                        Log.d(TAG, result)
                    }
                }
            })
    }

    fun onStart() {
        sharedPreferencesInteractor?.getFilmsHistory(object :
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
                    adapter.tracks = historyTracks
                    handler.post {
                        if (adapter.tracks.isNotEmpty()) {
                            tvHistoryHeader?.isVisible = true
                            clearHistory?.isVisible = true
                        } else {
                            tvHistoryHeader?.isVisible = false
                            clearHistory?.isVisible = false
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchSong(songTitle: String) {
        showProgressBar()

        tracksInteractor.searchTracks(songTitle, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                handler.post {
                    progressBar?.isVisible = false
                    if (foundTracks != null) {
                        adapter.tracks = tracks
                        refreshButton?.isVisible = false
                        noConnection?.isVisible = false
                        nothingFound?.isVisible = false
                        tracks.clear()
                        tracks.addAll(foundTracks)
                        recycler?.isVisible = true
//                        adapter.notifyItemRangeChanged(0, tracks.size)
                        adapter.notifyDataSetChanged()
                    }
                    if (errorMessage != null) {
                        showErrorMessage(notFoundError = false)
                    } else if (tracks.isEmpty()) {
                        showErrorMessage(notFoundError = true)
                    } else {
                        hideErrorMessage()
                    }
                }
            }
        })

    }

    private fun showProgressBar() {
        recycler?.isVisible = false
        noConnection?.isVisible = false
        nothingFound?.isVisible = false
        refreshButton?.isVisible = false
        clearHistory?.isVisible = false
        tvHistoryHeader?.isVisible = false
        progressBar?.isVisible = true
    }

    private fun hideErrorMessage() {
        nothingFound?.isVisible = false
        noConnection?.isVisible = false
        refreshButton?.isVisible = false
    }

    private fun showErrorMessage(notFoundError: Boolean) {
        val size = tracks.size
        tracks.clear()
//        adapter.notifyItemRangeRemoved(0, size)
        adapter.notifyDataSetChanged()
        recycler?.isVisible = false
        if (notFoundError) {
            nothingFound?.isVisible = true
        } else {
            noConnection?.isVisible = true
            refreshButton?.isVisible = true
        }
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
        Log.d(TAG, historyTracks.toString())
//        adapter.notifyItemChanged(0)
        adapter.notifyDataSetChanged()

    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val TAG = "SearchController"
    }
}