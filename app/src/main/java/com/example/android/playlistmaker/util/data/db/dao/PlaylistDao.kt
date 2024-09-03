package com.example.android.playlistmaker.util.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.android.playlistmaker.util.data.db.entity.PlaylistEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackCrossRef
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlaylistDao {
    @Transaction
    @Query("SELECT * From playlists")
    abstract fun getPlaylistsWithTracks(): Flow<List<PlaylistWithTracksEntity>>

    @Upsert(PlaylistEntity::class)
    abstract suspend fun insertPlaylist(playlistEntity: PlaylistEntity): Long

    @Insert(PlaylistTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTrackEntity(playlistTrackEntity: PlaylistTrackEntity)

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    abstract suspend fun removeTrackEntity(trackId: Long)

    @Insert(PlaylistTrackCrossRef::class, onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPlaylistWithTrack(playlistTrackCrossRef: PlaylistTrackCrossRef)

    suspend fun insertPlaylistWithTrackTime(playlistTrackCrossRef: PlaylistTrackCrossRef) {
        insertPlaylistWithTrack(playlistTrackCrossRef.apply {
            createdAt = System.currentTimeMillis()
        })
    }

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :playlistId")
    abstract fun getPlaylistById(playlistId: Long): Flow<PlaylistWithTracksEntity>

    @Transaction
    @Query("SELECT * FROM playlist_tracks INNER JOIN playlisttrackcrossref ON playlist_tracks.trackId =  playlisttrackcrossref.trackId WHERE playlistId = :playlistId ORDER BY createdAt DESC;")
    abstract fun getTracksByOrderByPlaylistId(playlistId: Long): Flow<List<PlaylistTrackEntity>>

    @Transaction
    @Query("DELETE FROM playlistTrackCrossRef WHERE playlistId = :playlistId AND trackId = :trackId")
    abstract suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    @Query("SELECT COUNT(*) FROM playlistTrackCrossRef WHERE trackId = :trackId")
    abstract suspend fun countTrackInPlaylist(trackId: Long): Int

    @Query("DELETE FROM playlists WHERE playlistId = :playlistId")
    abstract suspend fun removePlaylist(playlistId: Long)

    @Query("DELETE FROM playlisttrackcrossref WHERE playlistId = :playlistId")
    abstract suspend fun simpleRemovePlaylist(playlistId: Long)

    @Query("DELETE FROM playlist_tracks WHERE trackId IN (SELECT trackId FROM playlist_tracks WHERE trackId NOT IN (SELECT trackId from playlisttrackcrossref))")
    abstract suspend fun cleanupTracks()
}