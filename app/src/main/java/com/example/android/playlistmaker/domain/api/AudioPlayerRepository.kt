package com.example.android.playlistmaker.domain.api

import com.example.android.playlistmaker.domain.models.State

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl(): State

    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit, onCompletePlay: () -> Unit)

    fun release()

    abstract val currentPosition: Int
}