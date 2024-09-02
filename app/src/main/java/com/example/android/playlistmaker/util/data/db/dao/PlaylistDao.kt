package com.example.android.playlistmaker.util.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
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
interface PlaylistDao {
    @Transaction
    @Query("SELECT * From playlists")
    fun getPlaylistsWithTracks(): Flow<List<PlaylistWithTracksEntity>>

    @Upsert(PlaylistEntity::class)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity): Long

    @Insert(PlaylistTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackEntity(playlistTrackEntity: PlaylistTrackEntity)

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun removeTrackEntity(trackId: Long)

    @Insert(PlaylistTrackCrossRef::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistWithTrack(playlistTrackCrossRef: PlaylistTrackCrossRef)

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :playlistId")
    fun getPlaylistById(playlistId: Long): Flow<PlaylistWithTracksEntity>

    @Transaction
    @Query("DELETE FROM playlistTrackCrossRef WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    @Query("SELECT COUNT(*) FROM playlistTrackCrossRef WHERE trackId = :trackId")
    suspend fun countTrackInPlaylist(trackId: Long): Int

    @Query("DELETE FROM playlists WHERE playlistId = :playlistId")
    suspend fun removePlaylist(playlistId: Long)

    @Query("DELETE FROM playlisttrackcrossref WHERE playlistId = :playlistId")
    suspend fun simpleRemovePlaylist(playlistId: Long)

    @Query("DELETE FROM playlist_tracks WHERE trackId IN (SELECT trackId FROM playlist_tracks WHERE trackId NOT IN (SELECT trackId from playlisttrackcrossref))")
    suspend fun cleanupTracks()
}