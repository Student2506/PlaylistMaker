package com.example.android.playlistmaker.player.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val primaryGenreName: String,
    val releaseDate: String?,
    val country: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false,
    val createdAt: Long = java.time.Instant.now().toEpochMilli(),
) : Parcelable
