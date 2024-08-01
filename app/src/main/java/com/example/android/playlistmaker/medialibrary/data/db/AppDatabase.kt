package com.example.android.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.playlistmaker.medialibrary.data.db.dao.FavoriteTrackDao
import com.example.android.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTrackDao
}