package com.example.android.playlistmaker.main

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor


class CustomApp : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val darkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = darkModeFlags == Configuration.UI_MODE_NIGHT_YES
        val sharedPreferencesInteractor = Creator.provideSharedPreferncesInteractor(this)
        sharedPreferencesInteractor.getDarkThemePref(object :
            SharedPreferencesInteractor.SharedPreferencesConsumer {
            override fun consume(result: Any) {
                if (result is Boolean) darkTheme = result
                else darkTheme = isDarkMode
            }
        })
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


}