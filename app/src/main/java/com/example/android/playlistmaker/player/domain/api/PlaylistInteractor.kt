package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun retrievePlaylists(): Flow<List<Playlist>>
}