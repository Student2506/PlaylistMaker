package com.example.android.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.playlistmaker.databinding.ActivitySearchBinding
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.search.presentation.TrackSearchViewModel
import com.example.android.playlistmaker.search.presentation.TracksState


class SearchActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var binding: ActivitySearchBinding? = null

    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            showTrack(it)
        }
    }
    private var trackSearchViewModel: TrackSearchViewModel? = null

    private var isClickAllowed = true

    private var lastRequest: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        trackSearchViewModel = ViewModelProvider(
            this,
            TrackSearchViewModel.getViewModelFactory()
        )[TrackSearchViewModel::class.java]
        trackSearchViewModel?.observeState()?.observe(this) {
            render(it)
        }

        binding?.flBackToMain?.setOnClickListener {
            finish()
        }
        binding?.ivClear?.setOnClickListener {
            binding?.etInput?.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding?.etInput?.windowToken, 0)
            trackSearchViewModel?.setHistoryTracks()
            if (adapter.tracks.isNotEmpty()) {
                binding?.tvHistoryHeader?.isVisible = true
                binding?.btClearHistory?.isVisible = true
            }

        }
        binding?.etInput?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding?.etInput?.text?.isNotEmpty() ?: false) {
                    binding?.tvHistoryHeader?.isVisible = false
                    binding?.btClearHistory?.isVisible = false
                    trackSearchViewModel?.searchSong(binding?.etInput?.text.toString())
                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding?.etInput?.windowToken, 0)
                true
            } else {
                false
            }
        }
        binding?.etInput?.addTextChangedListener(beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                binding?.ivClear?.isVisible = !charSequence.isNullOrEmpty()
                trackSearchViewModel?.searchDebounce(changedText = charSequence?.toString() ?: "")
            },
            afterTextChanged = { _ ->
            })
        binding?.etInput?.setOnFocusChangeListener { _, hasFocus ->
            binding?.tvHistoryHeader?.isVisible = false
            binding?.btClearHistory?.isVisible = false

            if (hasFocus && binding?.etInput?.text?.isEmpty() ?: false) {
                val size = adapter.tracks.size
                adapter.tracks = arrayListOf()
                adapter.notifyItemRangeRemoved(0, size)
            } else {
                trackSearchViewModel?.setSearchTracks()
            }
        }
        binding?.btRefresh?.setOnClickListener {
            trackSearchViewModel?.searchSong(lastRequest!!)
        }
        binding?.btClearHistory?.isVisible = false

        binding?.tvHistoryHeader?.isVisible = false
        binding?.btClearHistory?.setOnClickListener {
            trackSearchViewModel?.clearHistory()
            binding?.tvHistoryHeader?.isVisible = false
            binding?.btClearHistory?.isVisible = false
        }

        binding?.rvTracks?.layoutManager = LinearLayoutManager(this)
        binding?.rvTracks?.adapter = adapter

    }

    override fun onStop() {
        super.onStop()
        trackSearchViewModel?.onStop()
    }

    override fun onStart() {
        super.onStart()
        trackSearchViewModel?.onStart()
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
        trackSearchViewModel?.showTrack(track)
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


    private fun showLoading() {
        binding?.pbProgressBar?.isVisible = true
        binding?.tvNoSignal?.isVisible = false
        binding?.btRefresh?.isVisible = false
        binding?.tvNothingFound?.isVisible = false
        binding?.rvTracks?.isVisible = false
        binding?.btClearHistory?.isVisible = false
        binding?.tvHistoryHeader?.isVisible = false
    }

    private fun showError() {
        binding?.pbProgressBar?.isVisible = false
        binding?.tvNoSignal?.isVisible = true
        binding?.btRefresh?.isVisible = true
        binding?.tvNothingFound?.isVisible = false
        binding?.rvTracks?.isVisible = false
        binding?.btClearHistory?.isVisible = false
        binding?.tvHistoryHeader?.isVisible = false
    }

    private fun showEmpty() {
        binding?.pbProgressBar?.isVisible = false
        binding?.tvNoSignal?.isVisible = false
        binding?.btRefresh?.isVisible = false
        binding?.tvNothingFound?.isVisible = true
        binding?.rvTracks?.isVisible = false
        binding?.btClearHistory?.isVisible = false
        binding?.tvHistoryHeader?.isVisible = false
    }

    private fun showContent(tracks: List<Track>, isHistory: Boolean) {
        binding?.pbProgressBar?.isVisible = false
        binding?.tvNoSignal?.isVisible = false
        binding?.btRefresh?.isVisible = false
        binding?.tvNothingFound?.isVisible = false
        binding?.rvTracks?.isVisible = true
        binding?.btClearHistory?.isVisible = isHistory && tracks.isNotEmpty()
        binding?.tvHistoryHeader?.isVisible = isHistory && tracks.isNotEmpty()

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Empty -> showEmpty()
            is TracksState.ServerError -> showError()
            is TracksState.Content -> showContent(state.tracks, false)
            is TracksState.HistoryContent -> showContent(state.tracks, true)
        }
    }
}