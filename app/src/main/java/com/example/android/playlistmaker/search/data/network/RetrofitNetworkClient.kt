package com.example.android.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.android.playlistmaker.search.data.NetworkClient
import com.example.android.playlistmaker.search.data.dto.ITunesTrackRequest
import com.example.android.playlistmaker.search.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val itunesService: ITunesApiService,
    private val context: Context,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        Log.d(TAG, "Start request")
        if (isConnected() == false) {
            Log.e(TAG, "No network")
            return Response().apply { resultCode = -1 }
        }
        if (dto !is ITunesTrackRequest) {
            Log.e(TAG, "Error dto is: ${dto::class.qualifiedName}")
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = itunesService.searchTracks(term = dto.term, entity = dto.entity)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTrace.toString())
                Response().apply { resultCode = 500 }
            }
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