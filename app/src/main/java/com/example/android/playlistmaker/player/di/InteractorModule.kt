package com.example.android.playlistmaker.player.di

import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }
}