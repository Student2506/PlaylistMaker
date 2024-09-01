package com.example.android.playlistmaker.util.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.playlistmaker.util.data.db.dao.FavoriteTrackDao
import com.example.android.playlistmaker.util.data.db.dao.PlaylistDao
import com.example.android.playlistmaker.util.data.db.entity.PlaylistEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackCrossRef
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackEntity
import com.example.android.playlistmaker.util.data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackCrossRef::class, PlaylistTrackEntity::class],
    autoMigrations = [],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
}