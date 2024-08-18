package com.example.android.playlistmaker.medialibrary.domain.impl

import com.example.android.playlistmaker.medialibrary.domain.db.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.db.PlaylistRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist): Flow<List<Playlist>> {
        return playlistRepository.createPlaylist(playlist)
    }
}