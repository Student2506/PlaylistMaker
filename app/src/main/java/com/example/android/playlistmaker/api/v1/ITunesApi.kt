package com.example.android.playlistmaker.api.v1

import com.example.android.playlistmaker.datalayer.ITunesTrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search")
    fun getTracks(
        @Query("term") term: String,
        @Query("entity") entity: String = "song"
    ): Call<ITunesTrackResponse>
}