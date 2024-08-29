package com.example.android.playlistmaker.medialibrary.di

import com.example.android.playlistmaker.medialibrary.data.FavoritesRepositoryImpl
import com.example.android.playlistmaker.medialibrary.data.PlaylistRepositoryImpl
import com.example.android.playlistmaker.medialibrary.data.converters.PlaylistConverter
import com.example.android.playlistmaker.medialibrary.data.converters.TrackConverter
import com.example.android.playlistmaker.medialibrary.domain.api.FavoritesRepository
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistRepository
import org.koin.dsl.module

val favoriteRepositoryModule = module {
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory {
        TrackConverter()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    factory {
        PlaylistConverter(get())
    }
}