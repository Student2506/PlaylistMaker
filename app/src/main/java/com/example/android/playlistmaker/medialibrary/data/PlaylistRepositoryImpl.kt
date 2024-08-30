package com.example.android.playlistmaker.medialibrary.data

import com.example.android.playlistmaker.medialibrary.data.converters.PlaylistConverter
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.util.data.db.AppDatabase
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConverter: PlaylistConverter,
) : PlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist): Boolean {
        val playlistEntity = playlistConverter.map(playlist)
        val inserted = appDatabase.playlistDao().insertPlaylist(playlistEntity)
        return inserted > 0
    }


    private fun convertFromPlaylistEntity(playlists: List<PlaylistWithTracksEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConverter.map(playlist) }
    }

    override suspend fun retrievePlaylists(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylistsWithTracks().map {
            convertFromPlaylistEntity(it)
        }

    override suspend fun retrievePlaylistById(playlistId: Long): Flow<Playlist> =
        appDatabase.playlistDao().getPlaylistById(playlistId = playlistId)
            .map { playlistConverter.map(it) }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        appDatabase.playlistDao().removeTrackFromPlaylist(playlistId, trackId)
    }
}