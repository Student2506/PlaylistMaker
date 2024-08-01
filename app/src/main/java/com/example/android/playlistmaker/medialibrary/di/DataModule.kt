package com.example.android.playlistmaker.medialibrary.di

import androidx.room.Room
import com.example.android.playlistmaker.medialibrary.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favoriteDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}