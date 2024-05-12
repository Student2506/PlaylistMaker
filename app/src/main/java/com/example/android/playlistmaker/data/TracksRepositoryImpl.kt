package com.example.android.playlistmaker.data

import com.example.android.playlistmaker.data.dto.ITunesTrackRequest
import com.example.android.playlistmaker.data.dto.ITunesTrackResponse
import com.example.android.playlistmaker.domain.api.TracksRepository
import com.example.android.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): List<Track> {
        val response = networkClient.doRequest(ITunesTrackRequest(term))
        if (response.resultCode == 200) {
            return (response as ITunesTrackResponse).tracks.map {
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
            }
        } else {
            return emptyList()
        }
    }
}