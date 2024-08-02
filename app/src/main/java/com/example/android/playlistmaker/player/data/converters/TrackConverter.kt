package com.example.android.playlistmaker.player.data.converters

import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.util.data.db.entity.TrackEntity

class TrackConverter {
    fun map(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                trackId,
                trackName,
                artistName,
                trackTime,
                artworkUrl100,
                collectionName,
                primaryGenreName,
                releaseDate ?: "",
                country,
                previewUrl,
                createdAt = createdAt
            )
        }
    }

    fun map(track: TrackEntity): Track {
        return with(track) {
            Track(
                trackId,
                trackName,
                artistName,
                trackTime,
                artworkUrl100,
                collectionName,
                primaryGenreName,
                releaseDate,
                country,
                previewUrl
            )
        }
    }
}