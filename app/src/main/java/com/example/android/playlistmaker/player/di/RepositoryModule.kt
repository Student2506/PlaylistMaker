package com.example.android.playlistmaker.player.di

import com.example.android.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import org.koin.dsl.module

val playerRepositoryModule = module {

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }
}