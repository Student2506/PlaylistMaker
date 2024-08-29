package com.example.android.playlistmaker.medialibrary.domain.api

import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist): Boolean
    suspend fun retreivePlaylists(): Flow<List<Playlist>>
}