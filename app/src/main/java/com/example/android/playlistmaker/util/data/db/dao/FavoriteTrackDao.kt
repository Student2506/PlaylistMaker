package com.example.android.playlistmaker.util.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.playlistmaker.util.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {

    @Query("SELECT * FROM favorite_tracks ORDER BY createdAt DESC;")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete
    suspend fun removeTrack(trackEntity: TrackEntity)

    @Query("SELECT COUNT(*) FROM favorite_tracks WHERE trackId = :trackId")
    suspend fun getFavoriteTrack(trackId: Long): Int
}