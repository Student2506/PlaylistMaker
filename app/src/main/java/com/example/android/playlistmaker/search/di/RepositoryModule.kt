package com.example.android.playlistmaker.search.di

import com.example.android.playlistmaker.search.data.SharedPreferncesRepositoryImpl
import com.example.android.playlistmaker.search.data.TracksRepositoryImpl
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesRepository
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import com.example.android.playlistmaker.search.domain.impl.SharedPreferencesInteractorImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SharedPreferencesRepository> {
        SharedPreferncesRepositoryImpl(get())
    }
}