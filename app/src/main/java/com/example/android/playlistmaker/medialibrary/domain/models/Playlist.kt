package com.example.android.playlistmaker.medialibrary.domain.models

data class Playlist(
    val id: Long?,
    val imageUrl: String?,
    val title: String,
    val description: String?,
    val tracks: List<Track>?,
)
