package com.example.android.playlistmaker.util

import com.example.android.playlistmaker.medialibrary.domain.models.Track as TrackLibrary
import com.example.android.playlistmaker.player.domain.models.Track as TrackPlayer
import com.example.android.playlistmaker.search.domain.models.Track as TrackSearch



class TrackConverter {
    fun map(track: TrackSearch): TrackPlayer {
        return with(track) {
            TrackPlayer(
                trackId,
                trackName,
                artistName,
                trackTime,
                artworkUrl100,
                collectionName,
                primaryGenreName,
                releaseDate ?: "",
                country,
                previewUrl
            )
        }
    }
    fun map(track: TrackLibrary): TrackPlayer {
        return with(track) {
            TrackPlayer(
                trackId,
                trackName,
                artistName,
                trackTime,
                artworkUrl100,
                collectionName,
                primaryGenreName,
                releaseDate ?: "",
                country,
                previewUrl
            )
        }
    }
}