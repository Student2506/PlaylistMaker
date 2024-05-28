package com.example.android.playlistmaker.search.presentation

import com.example.android.playlistmaker.search.domain.models.Track

interface TrackSearchView {

    fun showLoading()

    fun showError()

    fun showEmpty()

    fun showContent(tracks: List<Track>, isHistory: Boolean = false)

//    fun showServerError(isVisible: Boolean)
//
//    fun showNotFoundError(isVisible: Boolean)
//
//    fun showMovieList(isVisible: Boolean)
//
//    fun showProgressBar(isVisible: Boolean)
//
//    fun showHistoryList(isVisible: Boolean)

//    fun updateTracksList(tracksList: List<Track>)
}