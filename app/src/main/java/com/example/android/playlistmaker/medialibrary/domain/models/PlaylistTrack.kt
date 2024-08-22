package com.example.android.playlistmaker.medialibrary.domain.models

import com.google.gson.annotations.SerializedName

data class PlaylistTrack(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val primaryGenreName: String,
    val releaseDate: String?,
    val country: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false,
)
