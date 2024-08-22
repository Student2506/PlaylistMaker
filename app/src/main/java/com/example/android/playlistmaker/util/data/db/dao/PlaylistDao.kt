package com.example.android.playlistmaker.util.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Insert(PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity): Long

    @Insert(PlaylistTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackEntity(playlistTrackEntity: PlaylistTrackEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylistWithTrack(playlistTrackCrossRef: PlaylistTrackCrossRef)
}