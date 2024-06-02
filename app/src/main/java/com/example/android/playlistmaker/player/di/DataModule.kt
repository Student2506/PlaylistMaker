package com.example.android.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.android.playlistmaker.player.data.PlayerClient
import com.example.android.playlistmaker.player.data.player.AndroidStandardPlayerClient
import org.koin.dsl.module

val playerDataModule = module {
    single<PlayerClient> {
        AndroidStandardPlayerClient(get())
    }

    single {
        MediaPlayer()
    }
}