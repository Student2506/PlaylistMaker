package com.example.android.playlistmaker.creator

import android.app.Application
import com.example.android.playlistmaker.player.data.MediaPlayerClientImpl
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.search.data.SharedPreferncesRepositoryImpl
import com.example.android.playlistmaker.search.data.TracksRepositoryImpl
import com.example.android.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.android.playlistmaker.search.data.shared_preferences.SharedPreferncesClient
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesRepository
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import com.example.android.playlistmaker.search.domain.impl.SharedPreferencesInteractorImpl
import com.example.android.playlistmaker.search.domain.impl.TracksInteractorImpl

object Creator {

    private val audioPlayer: AudioPlayerRepository by lazy {
        MediaPlayerClientImpl()
    }

    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    fun provideAudioPlayer(): AudioPlayerRepository {
        return audioPlayer
    }

    private fun getSharedPreferncesRepository(application: Application): SharedPreferencesRepository {
        return SharedPreferncesRepositoryImpl(SharedPreferncesClient(application))
    }

    fun provideSharedPreferncesInteractor(application: Application): SharedPreferencesInteractor {
        return SharedPreferencesInteractorImpl(getSharedPreferncesRepository(application))
    }
}