package com.example.android.playlistmaker.search.data.network

import com.example.android.playlistmaker.search.data.dto.ITunesTrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("entity") entity: String = "song",
    ): ITunesTrackResponse
}