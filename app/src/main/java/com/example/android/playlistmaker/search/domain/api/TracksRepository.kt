package com.example.android.playlistmaker.search.domain.api

import com.example.android.playlistmaker.search.domain.models.Resource
import com.example.android.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(term: String): Flow<Resource<List<Track>>>
}