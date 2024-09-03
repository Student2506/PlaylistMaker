package com.example.android.playlistmaker.player.data

import android.database.sqlite.SQLiteConstraintException
import com.example.android.playlistmaker.player.data.converters.PlaylistConverter
import com.example.android.playlistmaker.player.data.converters.TrackConverter
import com.example.android.playlistmaker.player.domain.api.PlaylistRepository
import com.example.android.playlistmaker.player.domain.models.Playlist
import com.example.android.playlistmaker.player.domain.models.Status
import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.util.data.db.AppDatabase
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackCrossRef
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConverter: PlaylistConverter,
    private val trackConverter: TrackConverter,
) : PlaylistRepository {
    override suspend fun retrievePlaylists(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylistsWithTracks().map { playlists ->
            convertFromPlaylistEntity(playlists)
        }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistWithTracksEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConverter.map(playlist) }
    }

    override suspend fun insertTrack(playlistId: Long, track: Track): Status {
        try {
            appDatabase.playlistDao().insertTrackEntity(trackConverter.mapPlaylist(track))
            appDatabase.playlistDao().insertPlaylistWithTrack(
                PlaylistTrackCrossRef(
                    playlistId, track.trackId, System.currentTimeMillis()
                )
            )
        } catch (e: SQLiteConstraintException) {
            return Status.ExistFailure
        }
        return Status.Succses
    }

    override suspend fun createPlaylist(playlist: Playlist): Boolean {
        val playlistEntity = playlistConverter.mapPlaylist(playlist)
        val inserted = appDatabase.playlistDao().insertPlaylist(playlistEntity)
        return inserted > 0
    }

    companion object {
        private const val TAG = "PlaylistRepositoryImpl"
    }
}