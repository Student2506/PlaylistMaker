package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.data.converters.PlaylistConverter
import com.example.android.playlistmaker.player.domain.api.PlaylistRepository
import com.example.android.playlistmaker.player.domain.models.Playlist
import com.example.android.playlistmaker.util.data.db.AppDatabase
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConverter: PlaylistConverter,
) : PlaylistRepository {
    override suspend fun retrievePlaylists(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylistsWithTracks().map { playlists ->
            convertFromPlaylistEntity(playlists)
        }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistWithTracksEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConverter.map(playlist) }
    }

}