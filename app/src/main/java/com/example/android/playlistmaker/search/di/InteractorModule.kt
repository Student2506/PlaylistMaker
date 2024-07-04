package com.example.android.playlistmaker.search.di

import com.example.android.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.impl.HistoryTracksInteractorImpl
import com.example.android.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val historyTracksInteractorModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<HistoryTracksInteractor> {
        HistoryTracksInteractorImpl(get())
    }
}