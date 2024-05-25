package com.example.android.playlistmaker.search.data.network

import android.util.Log
import com.example.android.playlistmaker.search.data.NetworkClient
import com.example.android.playlistmaker.search.data.dto.ITunesTrackRequest
import com.example.android.playlistmaker.search.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val itunesService = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        Log.d(TAG, "request ${dto.toString()}")
        if (dto is ITunesTrackRequest) {
            val resp = itunesService.searchTracks(term = dto.term, entity = dto.entity).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            Log.d(TAG, "error!!!!!")
            return Response().apply { resultCode = 400 }
        }
    }

    companion object {
        private const val TAG = "RetrofitNetworkClient"
    }
}