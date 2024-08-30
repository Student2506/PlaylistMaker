package com.example.android.playlistmaker.medialibrary.domain.impl

import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist): Boolean {
        return playlistRepository.createPlaylist(playlist)
    }

    override suspend fun retreivePlaylists(): Flow<List<Playlist>> {
        return playlistRepository.retrievePlaylists()
    }

    override suspend fun retreivePlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistRepository.retrievePlaylistById(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        playlistRepository.removeTrackFromPlaylist(playlistId, trackId)
    }
}