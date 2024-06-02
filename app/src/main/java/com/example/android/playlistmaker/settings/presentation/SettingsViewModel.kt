package com.example.android.playlistmaker.settings.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.PlaylistMakerApp
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor

class SettingsViewModel(application: Application) :
    AndroidViewModel(application) {

    private var sharedPreferencesInteractor: SharedPreferencesInteractor =
        Creator.provideSharedPreferncesInteractor(getApplication<PlaylistMakerApp>())


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

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}