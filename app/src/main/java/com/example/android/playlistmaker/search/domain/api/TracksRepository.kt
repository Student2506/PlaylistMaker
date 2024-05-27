package com.example.android.playlistmaker.search.domain.api

import com.example.android.playlistmaker.search.domain.models.Resource
import com.example.android.playlistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(term: String): Resource<List<Track>>
}