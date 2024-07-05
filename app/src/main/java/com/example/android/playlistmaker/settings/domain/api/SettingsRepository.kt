package com.example.android.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun putDarkTheme(value: Boolean): Int
    fun getDarkTheme(): Boolean
}