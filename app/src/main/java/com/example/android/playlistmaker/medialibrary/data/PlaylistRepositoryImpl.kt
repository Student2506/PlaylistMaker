package com.example.android.playlistmaker.medialibrary.data

import android.util.Log
import com.example.android.playlistmaker.medialibrary.data.converters.PlaylistConverter
import com.example.android.playlistmaker.medialibrary.data.converters.TrackConverter
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import com.example.android.playlistmaker.util.data.db.AppDatabase
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConverter: PlaylistConverter,
    private val trackConverter: TrackConverter
) : PlaylistRepository {

    companion object {
        private const val TAG = "PlaylistRepositoryImpl"
    }

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
        val leftInPlaylist = appDatabase.playlistDao().countTrackInPlaylist(trackId)
        if (leftInPlaylist == 0) appDatabase.playlistDao().removeTrackEntity(trackId)
    }

    override suspend fun removePlaylist(playlistId: Long) {
        coroutineScope {
            val removeData = async { appDatabase.playlistDao().removePlaylist(playlistId) }
            val removeTheRest = async { appDatabase.playlistDao().cleanupTracks() }
            launch {
                removeData.await()
                removeTheRest.await()
            }
        }
    }

    override suspend fun retrieveTracksOrdered(playlistId: Long): Flow<List<PlaylistTrack>> {
        return appDatabase.playlistDao().getTracksByOrderByPlaylistId(playlistId).map {
            trackConverter.mapEntity(it)
        }
    }
}