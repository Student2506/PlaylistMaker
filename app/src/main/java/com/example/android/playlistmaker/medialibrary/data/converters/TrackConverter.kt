package com.example.android.playlistmaker.medialibrary.data.converters

import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import com.example.android.playlistmaker.medialibrary.domain.models.Track
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
                createdAt = createdAt,
            )
        }
    }

    fun mapPlaylist(track: PlaylistTrackEntity): PlaylistTrack {
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
                previewUrl,
                createdAt = createdAt
            )
        }
    }

    fun map(tracks: List<PlaylistTrack>): ArrayList<Track> {
        return ArrayList(tracks.map {
            with(it) {
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
                    previewUrl,
                    createdAt = 0L
                )
            }
        })
    }

    fun mapEntity(tracks: List<PlaylistTrackEntity>): ArrayList<PlaylistTrack> {
        return ArrayList(tracks.map {
            with(it) {
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
                    previewUrl,
                )
            }
        })
    }
}