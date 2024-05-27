package com.example.android.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.android.playlistmaker.search.data.NetworkClient
import com.example.android.playlistmaker.search.data.dto.ITunesTrackRequest
import com.example.android.playlistmaker.search.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val itunesService = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        Log.d(TAG, "Start request")
        if (isConnected() == false) {
            Log.e(TAG, "No network")
            return Response().apply { resultCode = -1 }
        }
        if (dto !is ITunesTrackRequest) {
            Log.d(TAG, "error!!!!!")
            return Response().apply { resultCode = 400 }
        }
        val response = itunesService.searchTracks(term = dto.term, entity = dto.entity).execute()
        Log.d(TAG, response.toString())
        val body = response.body()
        Log.d(TAG, body.toString())
        return if (body != null) {
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object {
        private const val TAG = "RetrofitNetworkClient"
    }
}