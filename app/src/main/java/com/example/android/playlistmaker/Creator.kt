package com.example.android.playlistmaker

import com.example.android.playlistmaker.data.TracksRepositoryImpl
import com.example.android.playlistmaker.data.network.RetrofitNetworkClient
import com.example.android.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.domain.api.TracksInteractor
import com.example.android.playlistmaker.domain.api.TracksRepository
import com.example.android.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.android.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private val audioPlayer: AudioPlayerInteractor by lazy {
        AudioPlayerInteractorImpl()
    }

    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    fun provideAudioPlayer(): AudioPlayerInteractor {
        return audioPlayer
    }
}