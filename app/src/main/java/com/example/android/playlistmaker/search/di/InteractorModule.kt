package com.example.android.playlistmaker.search.di

import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.impl.SharedPreferencesInteractorImpl
import com.example.android.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SharedPreferencesInteractor> {
        SharedPreferencesInteractorImpl(get())
    }
}