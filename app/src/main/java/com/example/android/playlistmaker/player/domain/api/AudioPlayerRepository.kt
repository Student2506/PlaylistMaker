package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.State

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl(): State

    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit, onCompletePlay: () -> Unit)

    fun release()

    abstract val currentPosition: Int
}