package com.example.android.playlistmaker.util.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.playlistmaker.util.data.db.dao.FavoriteTrackDao
import com.example.android.playlistmaker.util.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTrackDao
}