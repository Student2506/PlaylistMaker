package com.example.android.playlistmaker.player.domain.models

sealed interface State {
    object Default : State
    object Playing : State
    object Paused : State
    object Prepared : State
}