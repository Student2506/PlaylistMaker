package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun retrievePlaylists(): Flow<List<Playlist>>
}