package com.example.android.playlistmaker.creator

import com.example.android.playlistmaker.search.data.TracksRepositoryImpl
import com.example.android.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import com.example.android.playlistmaker.player.data.MediaPlayerClientImpl
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
}