package com.example.android.playlistmaker

import com.example.android.playlistmaker.data.TracksRepositoryImpl
import com.example.android.playlistmaker.data.network.RetrofitNetworkClient
import com.example.android.playlistmaker.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.domain.api.TracksInteractor
import com.example.android.playlistmaker.domain.api.TracksRepository
import com.example.android.playlistmaker.data.MediaPlayerClientImpl
import com.example.android.playlistmaker.domain.impl.TracksInteractorImpl

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
}