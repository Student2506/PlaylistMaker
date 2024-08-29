package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Playlist
import com.example.android.playlistmaker.player.domain.models.Status
import com.example.android.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun retrievePlaylists(): Flow<List<Playlist>>
    suspend fun insertTrack(playlistId: Long, track: Track): Status
    suspend fun createPlaylist(playlist: Playlist): Boolean
}