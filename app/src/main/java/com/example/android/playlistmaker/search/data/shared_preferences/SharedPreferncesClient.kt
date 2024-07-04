package com.example.android.playlistmaker.search.data.shared_preferences

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.example.android.playlistmaker.search.data.SettingsStorageClient
import com.example.android.playlistmaker.search.data.dto.GetHistoryTracksRequest
import com.example.android.playlistmaker.search.data.dto.HistoryTracksResponse
import com.example.android.playlistmaker.search.data.dto.PutHistoryTracksRequest
import com.example.android.playlistmaker.search.data.dto.Response

class SharedPreferncesClient(context: Context) : SettingsStorageClient {

    private val sharedPrefernces = context.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE
    )

    override fun doRequest(dto: Any): Response {
        if (dto is GetHistoryTracksRequest) {
            val result = sharedPrefernces.getString(KEY_FOR_SEARCH_HISTORY, "")
            return HistoryTracksResponse(result!!)
        } else if (dto is PutHistoryTracksRequest) {
            sharedPrefernces.edit {
                putString(KEY_FOR_SEARCH_HISTORY, dto.value)
            }
            return Response().apply { resultCode = 0 }
        }
        return Response().apply { resultCode = 400 }
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val KEY_FOR_SEARCH_HISTORY = "key_for_search_activity"
    }
}