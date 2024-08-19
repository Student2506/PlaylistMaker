package com.example.android.playlistmaker.medialibrary.domain.db

import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist): Boolean
    suspend fun retrievePlaylists(): Flow<List<Playlist>>
}