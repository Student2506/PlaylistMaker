package com.example.android.playlistmaker.settings.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.main.CustomApp
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor

class SettingsViewModel(application: Application) :
    AndroidViewModel(application) {

    private var sharedPreferencesInteractor: SharedPreferencesInteractor? = null

    fun onCreate() {
        sharedPreferencesInteractor =
            Creator.provideSharedPreferncesInteractor(getApplication<CustomApp>())
    }

    fun themeSwitcher(checked: Boolean) {
        sharedPreferencesInteractor?.putDarkThemePref(
            checked,
            object : SharedPreferencesInteractor.SharedPreferencesConsumer {
                override fun consume(result: Any) {
                    Log.d(TAG, result.toString())
                }
            })

    }

    companion object {
        private const val TAG = "SettingsController"
    }
}