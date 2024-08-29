package com.example.android.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.android.playlistmaker.medialibrary.di.createPlaylistViewModelModule
import com.example.android.playlistmaker.medialibrary.di.favoriteDataModule
import com.example.android.playlistmaker.medialibrary.di.favoriteRepositoryModule
import com.example.android.playlistmaker.medialibrary.di.favoriteTracksViewModel
import com.example.android.playlistmaker.medialibrary.di.favoritesInteractorModule
import com.example.android.playlistmaker.medialibrary.di.playlistViewModelModule
import com.example.android.playlistmaker.player.di.playerDataModule
import com.example.android.playlistmaker.player.di.playerInteractorModule
import com.example.android.playlistmaker.player.di.playerRepositoryModule
import com.example.android.playlistmaker.player.di.playerViewModelModule
import com.example.android.playlistmaker.search.di.historyTrackRepositoryModule
import com.example.android.playlistmaker.search.di.historyTracksDataModule
import com.example.android.playlistmaker.search.di.historyTracksInteractorModule
import com.example.android.playlistmaker.search.di.viewModelModule
import com.example.android.playlistmaker.settings.di.settingsDataModule
import com.example.android.playlistmaker.settings.di.settingsInteractorModule
import com.example.android.playlistmaker.settings.di.settingsRepositoryModule
import com.example.android.playlistmaker.settings.di.settingsViewModelModule
import com.example.android.playlistmaker.settings.domain.api.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PlaylistMakerApp : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaylistMakerApp)
            modules(
                historyTracksDataModule,
                historyTrackRepositoryModule,
                historyTracksInteractorModule,
                viewModelModule,
                settingsViewModelModule,
                playerDataModule,
                playerRepositoryModule,
                playerInteractorModule,
                playerViewModelModule,
                settingsDataModule,
                settingsRepositoryModule,
                settingsInteractorModule,
                favoriteDataModule,
                favoriteTracksViewModel,
                favoritesInteractorModule,
                playlistViewModelModule,
                favoriteRepositoryModule,
                createPlaylistViewModelModule
            )
        }
        val darkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = darkModeFlags == Configuration.UI_MODE_NIGHT_YES
        val settingsInteractor: SettingsInteractor by inject()
        settingsInteractor.getDarkThemePref(object : SettingsInteractor.SettingsConsumer {
            override fun consume(result: Any) {
                if (result is Boolean) darkTheme = result
                else darkTheme = isDarkMode
                switchTheme(darkTheme)
            }
        })
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