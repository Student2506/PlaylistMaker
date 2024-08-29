package com.example.android.playlistmaker.medialibrary.data

import com.example.android.playlistmaker.medialibrary.data.converters.TrackConverter
import com.example.android.playlistmaker.medialibrary.domain.api.FavoritesRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import com.example.android.playlistmaker.util.data.db.AppDatabase
import com.example.android.playlistmaker.util.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackConverter: TrackConverter,
) : FavoritesRepository {
    override suspend fun favoritesTracks(): Flow<List<Track>> =
        appDatabase.favoriteTracksDao().getFavoriteTracks().map {
            convertFromTrackEntity(it)
        }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackConverter.map(track) }
    }
}