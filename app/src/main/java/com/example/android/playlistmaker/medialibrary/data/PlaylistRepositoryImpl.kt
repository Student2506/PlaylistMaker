package com.example.android.playlistmaker.medialibrary.data

import android.util.Log
import com.example.android.playlistmaker.medialibrary.data.converters.PlaylistConverter
import com.example.android.playlistmaker.medialibrary.domain.db.PlaylistRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.util.data.db.AppDatabase
import com.example.android.playlistmaker.util.data.db.entity.PlaylistEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConverter: PlaylistConverter,
) : PlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist): Flow<List<Playlist>> {

        val playlistEntity = playlistConverter.map(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
        return appDatabase.playlistDao().getPlaylistsWithTracks().map {
            convertFromPlaylistEntity(it)
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistWithTracksEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConverter.map(playlist) }
    }
}