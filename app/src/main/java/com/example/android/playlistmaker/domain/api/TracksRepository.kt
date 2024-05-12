package com.example.android.playlistmaker.domain.api

import com.example.android.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(term: String): List<Track>
}