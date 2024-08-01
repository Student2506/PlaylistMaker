package com.example.android.playlistmaker.player.di

import android.media.MediaPlayer
import androidx.room.Room
import com.example.android.playlistmaker.player.data.PlayerClient
import com.example.android.playlistmaker.player.data.db.AppDatabase
import com.example.android.playlistmaker.player.data.player.AndroidStandardPlayerClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val playerDataModule = module {
    single<PlayerClient> {
        AndroidStandardPlayerClient(get())
    }

    single {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}