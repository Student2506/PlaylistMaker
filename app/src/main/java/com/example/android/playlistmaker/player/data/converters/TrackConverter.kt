package com.example.android.playlistmaker.player.data.converters

import com.example.android.playlistmaker.player.domain.models.PlaylistTrack
import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.util.data.db.entity.PlaylistTrackEntity
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

    fun mapPlaylist(track: Track): PlaylistTrackEntity {
        return with(track) {
            PlaylistTrackEntity(
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

    fun mapPlaylist(track: PlaylistTrackEntity): Track {
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

    fun map(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return with(playlistTrack) {
            PlaylistTrackEntity(
                trackId,
                trackName,
                artistName,
                trackTime,
                artworkUrl100,
                collectionName,
                primaryGenreName,
                releaseDate ?: "0000-00-00",
                country,
                previewUrl,
            )
        }
    }
    fun mapPlaylistTrack(track: PlaylistTrackEntity): PlaylistTrack {
        return with(track) {
            PlaylistTrack(
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