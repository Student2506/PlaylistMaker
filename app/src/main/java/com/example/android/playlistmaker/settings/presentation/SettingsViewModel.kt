package com.example.android.playlistmaker.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsViewModel(private var settingsInteractor: SettingsInteractor) : ViewModel() {

    fun themeSwitcher(checked: Boolean) {
        settingsInteractor.putDarkThemePref(checked, object : SettingsInteractor.SettingsConsumer {
            override fun consume(result: Any) {
                Log.d(TAG, result.toString())
            }

        })
    }

    companion object {
        private const val TAG = "SettingsController"
    }
}