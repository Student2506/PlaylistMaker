package com.example.android.playlistmaker.search.domain.api

import com.example.android.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(term: String): Flow<Pair<List<Track>?, String?>>
}