package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.domain.db.FavoritesInteractor
import com.example.android.playlistmaker.medialibrary.domain.impl.FavoritesInteractorImpl
import org.koin.dsl.module

val favoritesInteractorModule = module {
    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}