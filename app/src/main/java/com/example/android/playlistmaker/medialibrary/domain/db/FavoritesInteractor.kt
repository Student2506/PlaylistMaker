package com.example.android.playlistmaker.medialibrary.domain.db

import com.example.android.playlistmaker.medialibrary.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun favoriteTracks(): Flow<List<Track>>
}