package com.example.android.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.search.di.dataModule
import com.example.android.playlistmaker.search.di.interactorModule
import com.example.android.playlistmaker.search.di.repositoryModule
import com.example.android.playlistmaker.search.di.viewModelModule
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PlaylistMakerApp : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaylistMakerApp)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        val darkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = darkModeFlags == Configuration.UI_MODE_NIGHT_YES
        val sharedPreferencesInteractor: SharedPreferencesInteractor by inject()
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