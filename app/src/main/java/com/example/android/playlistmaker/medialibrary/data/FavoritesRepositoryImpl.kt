package com.example.android.playlistmaker.medialibrary.data

import com.example.android.playlistmaker.medialibrary.data.converters.TrackConverter
import com.example.android.playlistmaker.medialibrary.domain.db.FavoritesRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import com.example.android.playlistmaker.util.data.db.AppDatabase
import com.example.android.playlistmaker.util.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackConverter: TrackConverter,
) : FavoritesRepository {
    override fun favoritesTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = appDatabase.favoriteTracksDao().getFavoriteTracks()
        emit(convertFromTrackEntity(favoriteTracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackConverter.map(track) }
    }
}