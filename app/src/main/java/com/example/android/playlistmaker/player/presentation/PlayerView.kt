package com.example.android.playlistmaker.player.presentation

import com.example.android.playlistmaker.search.domain.models.Track

interface PlayerView {

    fun render(state: PlayerState)

    fun updateElapsedTime(time: Long)
}