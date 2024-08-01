package com.example.android.playlistmaker.search.data

import com.example.android.playlistmaker.search.data.dto.ITunesTrackRequest
import com.example.android.playlistmaker.search.data.dto.ITunesTrackResponse
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import com.example.android.playlistmaker.search.domain.models.Resource
import com.example.android.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(ITunesTrackRequest(term))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Check network connection"))
            }

            200 -> {
                with(response as ITunesTrackResponse) {
                    val data = response.tracks.map {
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
                            it.previewUrl,
                        )
                    }
                    emit(Resource.Success(data = data))
                }
            }

            else -> {
                emit(Resource.Error("Server error"))
            }
        }
    }
}