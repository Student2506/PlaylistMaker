package com.example.android.playlistmaker.medialibrary.domain.api

import com.example.android.playlistmaker.medialibrary.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun favoritesTracks(): Flow<List<Track>>
}