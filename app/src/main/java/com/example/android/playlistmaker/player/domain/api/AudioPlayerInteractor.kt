package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.State

interface AudioPlayerInteractor {
    fun startPlayer()

    fun pausePlayer()

    fun playbackControl(): State

    fun preparePlayer(trackUrl: String)

    fun getTrackTime(): Int
}