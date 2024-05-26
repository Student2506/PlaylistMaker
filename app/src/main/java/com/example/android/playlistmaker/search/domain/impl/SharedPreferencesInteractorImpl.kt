package com.example.android.playlistmaker.search.domain.impl

import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesRepository
import java.util.concurrent.Executors

class SharedPreferencesInteractorImpl(private val repository: SharedPreferencesRepository) :
    SharedPreferencesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun putFilmsHistory(
        films: String,
        consumer: SharedPreferencesInteractor.SharedPreferencesConsumer,
    ) {
        executor.execute {
            consumer.consume(repository.putValue(KEY_FOR_SEARCH_HISTORY, films))
        }
    }

    override fun getFilmsHistory(consumer: SharedPreferencesInteractor.SharedPreferencesConsumer) {
        executor.execute {
            consumer.consume(repository.getValue(KEY_FOR_SEARCH_HISTORY))
        }
    }

    override fun putDarkThemePref(
        darkTheme: Boolean,
        consumer: SharedPreferencesInteractor.SharedPreferencesConsumer,
    ) {
        executor.execute {
            consumer.consume(repository.putValue(KEY_FOR_NIGHT_THEME, darkTheme))
        }
    }

    override fun getDarkThemePref(consumer: SharedPreferencesInteractor.SharedPreferencesConsumer) {
        executor.execute {
            consumer.consume(repository.getValue(KEY_FOR_NIGHT_THEME))
        }
    }

    companion object {
        const val KEY_FOR_NIGHT_THEME = "key_for_night_theme"
        const val KEY_FOR_SEARCH_HISTORY = "key_for_search_activity"
    }
}