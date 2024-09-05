package com.example.android.playlistmaker.medialibrary.domain.impl

import android.util.Log
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    companion object {
        private const val TAG = "PlaylistInteractorImpl"
    }

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

    override suspend fun removePlaylist(playlistId: Long) {
        Log.d(TAG, "Interactor deleting list $playlistId")
        playlistRepository.removePlaylist(playlistId)
    }

    override suspend fun retrieveTracksOrdered(playlistId: Long): Flow<List<PlaylistTrack>> {
        return playlistRepository.retrieveTracksOrdered(playlistId)
    }
}