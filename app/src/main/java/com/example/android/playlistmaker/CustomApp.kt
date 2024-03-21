package com.example.android.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate


class CustomApp : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val darkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = darkModeFlags == Configuration.UI_MODE_NIGHT_YES
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(KEY_FOR_NIGHT_THEME, isDarkMode)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val KEY_FOR_NIGHT_THEME = "key_for_night_theme"
        const val KEY_FOR_SEARCH_HISTORY = "key_for_search_activity"
    }
}