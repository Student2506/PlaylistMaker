package com.example.android.playlistmaker.data.network

import com.example.android.playlistmaker.data.dto.ITunesTrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search")
    fun searchTracks(
        @Query("term") term: String,
        @Query("entity") entity: String = "song"
    ): Call<ITunesTrackResponse>
}