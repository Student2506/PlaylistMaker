package com.example.android.playlistmaker.datalayer

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String
)
