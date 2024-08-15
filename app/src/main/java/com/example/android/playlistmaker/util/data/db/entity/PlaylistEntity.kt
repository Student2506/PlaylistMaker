package com.example.android.playlistmaker.util.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val playlistId: Long,
    val playlistTitle: String,
    val playlistDescription: String?,
    val coverPath: String,
)