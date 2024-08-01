package com.example.android.playlistmaker.search.domain.impl

import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import com.example.android.playlistmaker.search.domain.models.Resource
import com.example.android.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val respository: TracksRepository) : TracksInteractor {

    override fun searchTracks(term: String): Flow<Pair<List<Track>?, String?>> {
        return respository.searchTracks(term).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}