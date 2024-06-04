package com.example.android.playlistmaker.search.di

import com.example.android.playlistmaker.search.data.NetworkClient
import com.example.android.playlistmaker.search.data.SettingsStorageClient
import com.example.android.playlistmaker.search.data.network.ITunesApiService
import com.example.android.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.android.playlistmaker.search.data.shared_preferences.SharedPreferncesClient
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApiService> {

        Retrofit.Builder().baseUrl("https://itunes.apple.com").addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(ITunesApiService::class.java)

    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<SettingsStorageClient> {
        SharedPreferncesClient(androidContext())
    }
}