package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.data.FavoritesRepositoryImpl
import com.example.android.playlistmaker.medialibrary.data.converters.TrackConverter
import com.example.android.playlistmaker.medialibrary.domain.db.FavoritesRepository
import org.koin.dsl.module

val favoriteRepositoryModule = module {
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory {
        TrackConverter()
    }
}