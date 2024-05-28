package com.example.android.playlistmaker.player.presentation

import com.example.android.playlistmaker.search.domain.models.Track

interface PlayerView {

    fun setupPosterImage(imageUrl: String)

    fun setupTextElements(track: Track)

    fun enableCollectionName(isVisible: Boolean)

    fun setupCollectionName(collectionName: String?)

    fun enableTrackPlay(isVisible: Boolean)

    fun setTime(trackDuration: String, trackPlayed: String)

    fun setYear(yearTrack: String)

    fun updateTrackTime(time: String)

    fun updateButtonPlayPause(isPlay: Boolean)
}