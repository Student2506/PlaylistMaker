package com.example.android.playlistmaker.medialibrary.data.converters

import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.util.data.db.entity.PlaylistEntity
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity

class PlaylistConverter(
    private val trackConverter: TrackConverter,
) {
    fun map(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                playlistId = 0,
                playlistTitle = title,
                playlistDescription = description,
                coverPath = imageUrl,
            )
        }
    }

    fun map(playlistEntity: PlaylistWithTracksEntity): Playlist {
        return with(playlistEntity) {
            Playlist(id = playlist.playlistId,
                title = playlist.playlistTitle,
                description = playlist.playlistDescription,
                imageUrl = playlist.coverPath,
                tracks = playlistEntity.tracks.map {
                    trackConverter.mapPlaylist(it)
                })
        }
    }


}