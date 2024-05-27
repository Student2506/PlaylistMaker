package com.example.android.playlistmaker.settings.ui

import android.app.Activity
import android.os.Bundle
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator


class SettingsActivity : Activity() {

    private val settingsController = Creator.provideSettingsControler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsController.onCreate()
    }

    companion object {
        private const val TAG = "SettingsActivity"
    }
}
