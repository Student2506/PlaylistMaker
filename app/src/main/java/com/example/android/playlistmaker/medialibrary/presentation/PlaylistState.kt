package com.example.android.playlistmaker.medialibrary.presentation

import com.example.android.playlistmaker.medialibrary.domain.models.Playlist

interface PlaylistState {
    data class Content(val playlist: List<Playlist>) : PlaylistState
    object Empty : PlaylistState
}