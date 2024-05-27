package com.example.android.playlistmaker.search.data

import com.example.android.playlistmaker.search.data.dto.ITunesTrackRequest
import com.example.android.playlistmaker.search.data.dto.ITunesTrackResponse
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import com.example.android.playlistmaker.search.domain.models.Resource
import com.example.android.playlistmaker.search.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): Resource<List<Track>> {
        val response = networkClient.doRequest(ITunesTrackRequest(term))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Check network connection")
            }

            200 -> {
                Resource.Success((response as ITunesTrackResponse).tracks.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTime,
                        it.artworkUrl100,
                        it.collectionName,
                        it.primaryGenreName,
                        it.releaseDate,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error("Server error")
            }
        }
    }
}