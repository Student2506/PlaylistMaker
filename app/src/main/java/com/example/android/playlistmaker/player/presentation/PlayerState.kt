package com.example.android.playlistmaker.player.presentation

import com.example.android.playlistmaker.search.domain.models.Track

sealed interface PlayerState {
    object Loading : PlayerState
    data class Content(val isPlay: Boolean) : PlayerState
}