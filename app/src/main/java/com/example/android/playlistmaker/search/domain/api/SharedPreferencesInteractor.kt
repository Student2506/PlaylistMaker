package com.example.android.playlistmaker.search.domain.api

interface SharedPreferencesInteractor {
    fun putFilmsHistory(films: String, consumer: SharedPreferencesConsumer)
    fun getFilmsHistory(consumer: SharedPreferencesConsumer)
    fun putDarkThemePref(darkTheme: Boolean, consumer: SharedPreferencesConsumer)
    fun getDarkThemePref(consumer: SharedPreferencesConsumer)

    interface SharedPreferencesConsumer {
        fun consume(result: Any)
    }
}