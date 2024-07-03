package com.example.android.playlistmaker.settings.domain.impl

import com.example.android.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.android.playlistmaker.settings.domain.api.SettingsRepository
import java.util.concurrent.Executors

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun putDarkThemePref(
        darkTheme: Boolean,
        consumer: SettingsInteractor.SettingsConsumer,
    ) {
        executor.execute {
            consumer.consume(repository.putDarkTheme(darkTheme))
        }
    }

    override fun getDarkThemePref(consumer: SettingsInteractor.SettingsConsumer) {
        executor.execute {
            consumer.consume(repository.getDarkTheme())
        }
    }


}
