package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.impl.FavoritesInteractorImpl
import com.example.android.playlistmaker.medialibrary.domain.impl.PlaylistInteractorImpl
import org.koin.dsl.module

val favoritesInteractorModule = module {
    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}