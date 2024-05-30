package com.example.android.playlistmaker.player.presentation

sealed interface PlayerState {
    object isLoaded : PlayerState
    data class Content(val isPlay: Boolean) : PlayerState
}