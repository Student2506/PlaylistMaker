package com.example.android.playlistmaker.search.data.shared_preferences

import android.app.Application
import androidx.core.content.edit
import com.example.android.playlistmaker.search.data.SettingsStorageClient
import com.example.android.playlistmaker.search.data.dto.Response
import com.example.android.playlistmaker.search.data.dto.SharedPreferencesRequest
import com.example.android.playlistmaker.search.data.dto.SharedPreferencesResponse

class SharedPreferncesClient(application: Application) : SettingsStorageClient {


    private val sharedPrefernces = application.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE
    )

    override fun doRequest(dto: Any): Response {
        if (dto is SharedPreferencesRequest) {
            if (dto.value != null) {
                if (dto.term == KEY_FOR_NIGHT_THEME && dto.value is Boolean) {
                    sharedPrefernces.edit {
                        putBoolean(KEY_FOR_NIGHT_THEME, dto.value)
                    }
                    return Response().apply { resultCode = 0 }
                }
                if (dto.term == KEY_FOR_SEARCH_HISTORY && dto.value is String) {
                    sharedPrefernces.edit {
                        putString(KEY_FOR_SEARCH_HISTORY, dto.value)
                    }
                    return Response().apply { resultCode = 0 }
                }
            } else {
                if (dto.term == KEY_FOR_NIGHT_THEME) {
                    return SharedPreferencesResponse(
                        sharedPrefernces.getBoolean(
                            KEY_FOR_NIGHT_THEME, false
                        )
                    )
                }
                if (dto.term == KEY_FOR_SEARCH_HISTORY) {
                    val result = sharedPrefernces.getString(KEY_FOR_SEARCH_HISTORY, "")
                    return SharedPreferencesResponse(result!!)
                }
            }

        }
        return Response().apply { resultCode = 400 }
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val KEY_FOR_NIGHT_THEME = "key_for_night_theme"
        const val KEY_FOR_SEARCH_HISTORY = "key_for_search_activity"
    }
}