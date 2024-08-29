package com.example.android.playlistmaker.player.di

import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.android.playlistmaker.player.domain.impl.PlaylistInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}