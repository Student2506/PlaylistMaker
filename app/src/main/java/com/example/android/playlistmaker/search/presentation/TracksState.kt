package com.example.android.playlistmaker.search.presentation

import com.example.android.playlistmaker.search.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(val tracks: List<Track>) : TracksState

    data class HistoryContent(val tracks: List<Track>) : TracksState

    object ServerError : TracksState

    object Empty : TracksState
}