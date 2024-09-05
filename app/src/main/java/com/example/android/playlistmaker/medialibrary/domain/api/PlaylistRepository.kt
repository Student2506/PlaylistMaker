package com.example.android.playlistmaker.medialibrary.domain.api

import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist): Boolean
    suspend fun retrievePlaylists(): Flow<List<Playlist>>
    suspend fun retrievePlaylistById(playlistId: Long): Flow<Playlist>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)
    suspend fun removePlaylist(playlistId: Long)
    suspend fun retrieveTracksOrdered(playlistId: Long): Flow<List<PlaylistTrack>>
}