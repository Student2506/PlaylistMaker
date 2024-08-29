package com.example.android.playlistmaker.player.di

import com.example.android.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.example.android.playlistmaker.player.data.PlaylistRepositoryImpl
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.api.PlaylistRepository
import org.koin.dsl.module

val playerRepositoryModule = module {

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get(), get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

}