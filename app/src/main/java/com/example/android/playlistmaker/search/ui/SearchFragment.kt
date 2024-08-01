package com.example.android.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.playlistmaker.databinding.FragmentSearchBinding
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.search.presentation.TrackSearchViewModel
import com.example.android.playlistmaker.search.presentation.TracksState
import com.example.android.playlistmaker.util.TrackConverter
import com.example.android.playlistmaker.util.debounce
import com.example.android.playlistmaker.util.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val adapter = TrackAdapter { track ->
        onTrackClickDebounce(track)
    }
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val trackSearchViewModel by viewModel<TrackSearchViewModel>()
    private var lastRequest: String? = null
    private var isHistory = true

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            showTrack(track)
        }
        trackSearchViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.ivClear.setOnClickListener {
            binding.etInput.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.etInput.windowToken, 0)
            trackSearchViewModel.setHistoryTracks()
            if (adapter.tracks.isNotEmpty()) {
                binding.tvHistoryHeader.isVisible = true
                binding.btClearHistory.isVisible = true
            }
            isHistory = true
        }
        binding.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.etInput.text.isNotEmpty()) {
                    binding.tvHistoryHeader.isVisible = false
                    binding.btClearHistory.isVisible = false
                    isHistory = false
                    trackSearchViewModel.searchSong(binding.etInput.text.toString())
                }
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.etInput.windowToken, 0)
                true
            } else {
                false
            }
        }
        binding.etInput.addTextChangedListener(beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                binding.ivClear.isVisible = !charSequence.isNullOrEmpty()
                isHistory = false
                trackSearchViewModel.searchDebounce(changedText = charSequence?.toString() ?: "")
            },
            afterTextChanged = { _ ->
            })
        binding.etInput.setOnFocusChangeListener { _, hasFocus ->
            binding.tvHistoryHeader.isVisible = false
            binding.btClearHistory.isVisible = false

            if (hasFocus && binding.etInput.text.isEmpty()) {
                val size = adapter.tracks.size
                adapter.tracks = arrayListOf()
                adapter.notifyItemRangeRemoved(0, size)
            } else {
                trackSearchViewModel.setSearchTracks()
            }
        }
        binding.btRefresh.setOnClickListener {
            trackSearchViewModel.searchSong(lastRequest!!)
        }
        binding.btClearHistory.isVisible = false

        binding.tvHistoryHeader.isVisible = false
        binding.btClearHistory.setOnClickListener {
            trackSearchViewModel.clearHistory()
            binding.tvHistoryHeader.isVisible = false
            binding.btClearHistory.isVisible = false
        }

        binding.rvTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTracks.adapter = adapter
        trackSearchViewModel.setHistoryTracks()
    }

    private fun showTrack(track: Track) {
        trackSearchViewModel.showTrack(track, isHistory)
        val settingsIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
        val trackConverter: TrackConverter = TrackConverter()
        settingsIntent.putExtra(TRACK_TO_SHOW, trackConverter.map(track))
        startActivity(settingsIntent)
    }

    private fun showLoading() {
        binding.pbProgressBar.isVisible = true
        binding.tvNoSignal.isVisible = false
        binding.btRefresh.isVisible = false
        binding.tvNothingFound.isVisible = false
        binding.rvTracks.isVisible = false
        binding.btClearHistory.isVisible = false
        binding.tvHistoryHeader.isVisible = false
    }

    private fun showError() {
        binding.pbProgressBar.isVisible = false
        binding.tvNoSignal.isVisible = true
        binding.btRefresh.isVisible = true
        binding.tvNothingFound.isVisible = false
        binding.rvTracks.isVisible = false
        binding.btClearHistory.isVisible = false
        binding.tvHistoryHeader.isVisible = false
    }

    private fun showEmpty() {
        binding.pbProgressBar.isVisible = false
        binding.tvNoSignal.isVisible = false
        binding.btRefresh.isVisible = false
        binding.tvNothingFound.isVisible = true
        binding.rvTracks.isVisible = false
        binding.btClearHistory.isVisible = false
        binding.tvHistoryHeader.isVisible = false
    }

    private fun showContent(tracks: List<Track>, isHistory: Boolean) {
        binding.pbProgressBar.isVisible = false
        binding.tvNoSignal.isVisible = false
        binding.btRefresh.isVisible = false
        binding.tvNothingFound.isVisible = false
        binding.rvTracks.isVisible = true
        binding.btClearHistory.isVisible = isHistory && tracks.isNotEmpty()
        binding.tvHistoryHeader.isVisible = isHistory && tracks.isNotEmpty()

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

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()

        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val DEFAULT_QUERY = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val TRACK_TO_SHOW = "track_to_show"
        private const val TAG = "SearchFragment"
    }
}