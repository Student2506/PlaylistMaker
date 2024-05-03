package com.example.android.playlistmaker.domain.api

import com.example.android.playlistmaker.domain.models.Track

interface TracksInteractor {

    fun searchTracks(term: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}