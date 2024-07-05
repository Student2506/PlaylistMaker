package com.example.android.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.android.playlistmaker.search.data.NetworkClient
import com.example.android.playlistmaker.search.data.dto.ITunesTrackRequest
import com.example.android.playlistmaker.search.data.dto.ITunesTrackResponse
import com.example.android.playlistmaker.search.data.dto.Response

class RetrofitNetworkClient(
    private val itunesService: ITunesApiService,
    private val context: Context,
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        Log.d(TAG, "Start request")
        if (isConnected() == false) {
            Log.e(TAG, "No network")
            return Response().apply { resultCode = -1 }
        }
        if (dto !is ITunesTrackRequest) {
            Log.e(TAG, "Error dto is: ${dto::class.qualifiedName}")
            return Response().apply { resultCode = 400 }
        }

        val response: retrofit2.Response<ITunesTrackResponse> = try {
            itunesService.searchTracks(term = dto.term, entity = dto.entity).execute()
        } catch (exc: java.io.IOException) {
            Log.e(TAG, exc.stackTrace.toString())
            throw exc
        }
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