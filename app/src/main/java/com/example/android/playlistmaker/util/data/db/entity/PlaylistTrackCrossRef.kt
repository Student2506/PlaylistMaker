package com.example.android.playlistmaker.util.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        entity = PlaylistEntity::class,
        parentColumns = ["playlistId"],
        childColumns = ["playlistId"]
    )],
    indices = [Index("playlistId", "trackId")],
)
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val trackId: Long,
)