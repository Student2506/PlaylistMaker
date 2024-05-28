package com.example.android.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.search.presentation.TrackSearchPresenter
import com.example.android.playlistmaker.search.presentation.TrackSearchView
import com.google.android.material.button.MaterialButton


class SearchActivity : AppCompatActivity(), TrackSearchView {

    private var recycler: RecyclerView? = null
    private var nothingFound: TextView? = null
    private var refreshButton: Button? = null
    private var noConnection: TextView? = null
    private var lastRequest: String? = null
    private var progressBar: ProgressBar? = null
    private var clearHistory: MaterialButton? = null
    private var tvHistoryHeader: TextView? = null
    private var inputEditText: EditText? = null
    private var clearButton: ImageView? = null
    private var headerButton: FrameLayout? = null

    private val handler = Handler(Looper.getMainLooper())

    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            showTrack(it)
        }
    }
    private var trackSearchPresenter: TrackSearchPresenter? = null

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        trackSearchPresenter = Creator.provideTrackSearchPresenter(this, this)
        inputEditText = findViewById(R.id.etInput)
        clearButton = findViewById<ImageView>(R.id.ivClear)
        headerButton = findViewById<FrameLayout>(R.id.flBackToMain)
        recycler = findViewById(R.id.rvTracks)
        nothingFound = findViewById(R.id.tvNothingFound)
        refreshButton = findViewById(R.id.btRefresh)
        noConnection = findViewById(R.id.tvNoSignal)
        progressBar = findViewById(R.id.pbProgressBar)
        headerButton?.setOnClickListener {
            finish()
        }
        clearButton?.setOnClickListener {
            inputEditText?.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText?.windowToken, 0)
            trackSearchPresenter?.setHistoryTracks()
            if (adapter.tracks.isNotEmpty()) {
                tvHistoryHeader?.isVisible = true
                clearHistory?.isVisible = true
            }

        }
        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText?.text?.isNotEmpty() ?: false) {
                    tvHistoryHeader?.isVisible = false
                    clearHistory?.isVisible = false
                    trackSearchPresenter?.searchSong(inputEditText?.text.toString())
                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(inputEditText?.windowToken, 0)
                true
            } else {
                false
            }
        }
        inputEditText?.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                clearButton?.isVisible = !charSequence.isNullOrEmpty()
                trackSearchPresenter?.searchDebounce(changedText = charSequence?.toString() ?: "")
            },
            afterTextChanged = { _ ->
            })
        inputEditText?.setOnFocusChangeListener { _, hasFocus ->
            tvHistoryHeader?.isVisible = false
            clearHistory?.isVisible = false

            if (hasFocus && inputEditText?.text?.isEmpty() ?: false) {
                val size = adapter.tracks.size
                adapter.tracks = arrayListOf()
                adapter.notifyItemRangeRemoved(0, size)
            } else {
                trackSearchPresenter?.setSearchTracks()
            }
        }
        refreshButton?.setOnClickListener {
            trackSearchPresenter?.searchSong(lastRequest!!)
        }
        clearHistory = findViewById<MaterialButton?>(R.id.btClearHistory).also {
            it.isVisible = false
        }
        tvHistoryHeader = findViewById<TextView?>(R.id.tvHistoryHeader).also {
            it.isVisible = false
        }
        clearHistory?.setOnClickListener {
            trackSearchPresenter?.clearHistory()
            tvHistoryHeader?.isVisible = false
            clearHistory?.isVisible = false
        }

        recycler?.layoutManager = LinearLayoutManager(this)
        recycler?.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        trackSearchPresenter?.onStop()
    }

    override fun onStart() {
        super.onStart()
        trackSearchPresenter?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        trackSearchPresenter?.onDestroy()
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
        trackSearchPresenter?.showTrack(track)
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

    override fun showServerError(isVisible: Boolean) {
        noConnection?.isVisible = isVisible
        refreshButton?.isVisible = isVisible
    }

    override fun showNotFoundError(isVisible: Boolean) {
        nothingFound?.isVisible = isVisible
    }

    override fun showMovieList(isVisible: Boolean) {
        recycler?.isVisible = isVisible
    }

    override fun showProgressBar(isVisible: Boolean) {
        progressBar?.isVisible = isVisible
    }

    override fun showHistoryList(isVisible: Boolean) {
        clearHistory?.isVisible = isVisible
        tvHistoryHeader?.isVisible = isVisible
        recycler?.isVisible = isVisible
    }

    override fun updateTracksList(tracksList: List<Track>) {
        adapter.tracks.clear()
        adapter.tracks.addAll(tracksList)
        adapter.notifyDataSetChanged()
    }
}