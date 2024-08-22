package com.example.android.playlistmaker.player.domain.impl

import com.example.android.playlistmaker.player.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.player.domain.api.PlaylistRepository
import com.example.android.playlistmaker.player.domain.models.Playlist
import com.example.android.playlistmaker.player.domain.models.Status
import com.example.android.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun retrievePlaylists(): Flow<List<Playlist>> =
        playlistRepository.retrievePlaylists()

    override suspend fun insertTrack(playlistId: Long, track: Track): Status {
        return playlistRepository.insertTrack(playlistId, track)
    }
}