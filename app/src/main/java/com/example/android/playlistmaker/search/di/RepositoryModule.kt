package com.example.android.playlistmaker.search.di

import com.example.android.playlistmaker.search.data.HistoryTracksRepositoryImpl
import com.example.android.playlistmaker.search.data.TracksRepositoryImpl
import com.example.android.playlistmaker.search.domain.api.HistoryTracksRepository
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import org.koin.dsl.module

val historyTrackRepositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<HistoryTracksRepository> {
        HistoryTracksRepositoryImpl(get())
    }
}