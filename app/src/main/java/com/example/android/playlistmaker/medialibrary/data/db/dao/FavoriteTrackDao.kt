package com.example.android.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.android.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Dao
interface FavoriteTrackDao {

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getFavoriteTracks(): List<TrackEntity>
}