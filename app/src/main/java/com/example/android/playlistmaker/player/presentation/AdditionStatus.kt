package com.example.android.playlistmaker.player.presentation

sealed interface AdditionStatus {
    data class Success(val playlistTitle: String) : AdditionStatus
    data class Error(val playlistTitle: String) : AdditionStatus
}