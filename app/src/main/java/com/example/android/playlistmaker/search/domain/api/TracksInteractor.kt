package com.example.android.playlistmaker.search.domain.api

import com.example.android.playlistmaker.search.domain.models.Track

interface TracksInteractor {

    fun searchTracks(term: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}