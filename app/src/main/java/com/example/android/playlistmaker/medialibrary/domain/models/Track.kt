package com.example.android.playlistmaker.medialibrary.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
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
    val createdAt: Long,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Track) return false

        if (trackId != other.trackId) return false
        if (trackName != other.trackName) return false
        if (artistName != other.artistName) return false
        if (trackTime != other.trackTime) return false
        if (artworkUrl100 != other.artworkUrl100) return false
        if (collectionName != other.collectionName) return false
        if (primaryGenreName != other.primaryGenreName) return false
        if (releaseDate != other.releaseDate) return false
        if (country != other.country) return false
        if (previewUrl != other.previewUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trackId.hashCode()
        result = 31 * result + trackName.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + trackTime.hashCode()
        result = 31 * result + artworkUrl100.hashCode()
        result = 31 * result + collectionName.hashCode()
        result = 31 * result + primaryGenreName.hashCode()
        result = 31 * result + (releaseDate?.hashCode() ?: 0)
        result = 31 * result + country.hashCode()
        result = 31 * result + (previewUrl?.hashCode() ?: 0)
        return result
    }
}
