package com.example.android.playlistmaker.util.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.playlistmaker.util.data.db.dao.FavoriteTrackDao
import com.example.android.playlistmaker.util.data.db.dao.PlaylistDao
import com.example.android.playlistmaker.util.data.db.entity.PlaylistEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackCrossRef
import com.example.android.playlistmaker.util.data.db.entity.TrackEntity

@Database(
    version = 3,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackCrossRef::class],
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
}