package com.example.android.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun putDarkThemePref(darkTheme: Boolean, consumer: SettingsConsumer)
    fun getDarkThemePref(consumer: SettingsConsumer)

    interface SettingsConsumer {
        fun consume(result: Any)
    }
}