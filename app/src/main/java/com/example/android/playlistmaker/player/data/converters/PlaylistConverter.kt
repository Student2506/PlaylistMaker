package com.example.android.playlistmaker.player.data.converters

import com.example.android.playlistmaker.player.domain.models.Playlist
import com.example.android.playlistmaker.util.data.db.entity.PlaylistEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity

class PlaylistConverter(
    private val trackConverter: TrackConverter,
) {
    fun map(playlistEntity: PlaylistWithTracksEntity): Playlist {
        return with(playlistEntity.playlist) {
            Playlist(id = playlistId,
                title = playlistTitle,
                description = playlistDescription,
                imageUrl = coverPath,
                tracks = playlistEntity.tracks.map {
                    trackConverter.mapPlaylistTrack(it)
                })
        }
    }

    fun mapPlaylist(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                id ?: 0, title, description ?: "", imageUrl
            )
        }
    }
}