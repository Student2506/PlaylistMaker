package com.example.android.playlistmaker.creator

import android.app.Activity
import android.app.Application
import com.example.android.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.example.android.playlistmaker.player.data.player.AndroidStandardPlayerClient
import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
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
import com.example.android.playlistmaker.search.presentation.SearchController
import com.example.android.playlistmaker.search.ui.TrackAdapter

object Creator {


    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }


    private fun getSharedPreferncesRepository(application: Application): SharedPreferencesRepository {
        return SharedPreferncesRepositoryImpl(SharedPreferncesClient(application))
    }

    fun provideSharedPreferncesInteractor(application: Application): SharedPreferencesInteractor {
        return SharedPreferencesInteractorImpl(getSharedPreferncesRepository(application))
    }

    private fun getPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(AndroidStandardPlayerClient())
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getPlayerRepository())
    }

    fun provideSearchController(activity: Activity, adapter: TrackAdapter): SearchController {
        return SearchController(activity, adapter)
    }
}