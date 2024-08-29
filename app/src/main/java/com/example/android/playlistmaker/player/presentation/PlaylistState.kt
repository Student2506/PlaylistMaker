package com.example.android.playlistmaker.player.presentation

import com.example.android.playlistmaker.player.domain.models.Playlist

sealed interface PlaylistState {
    data class Content(val playlist: List<Playlist>) : PlaylistState
    object Empty : PlaylistState
}