package com.example.android.playlistmaker.player.presentation

interface PlayerView {
    fun changePlayButton(isPlay: Boolean)

    fun updateElapsedTime(time: Long)
}