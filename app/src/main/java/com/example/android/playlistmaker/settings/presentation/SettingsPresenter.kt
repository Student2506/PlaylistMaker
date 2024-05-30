package com.example.android.playlistmaker.settings.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.FrameLayout
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.main.CustomApp
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.settings.ui.SettingsActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsPresenter(private val view: SettingsView, private val context: Context) {

    private var sharedPreferencesInteractor: SharedPreferencesInteractor? = null

    fun onCreate() {
        sharedPreferencesInteractor = Creator.provideSharedPreferncesInteractor(context)
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