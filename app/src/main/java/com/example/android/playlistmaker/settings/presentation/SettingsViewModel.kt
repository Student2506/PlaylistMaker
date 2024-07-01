package com.example.android.playlistmaker.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor

class SettingsViewModel(private var sharedPreferencesInteractor: SharedPreferencesInteractor) :
    ViewModel() {

    fun themeSwitcher(checked: Boolean) {
        sharedPreferencesInteractor.putDarkThemePref(
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