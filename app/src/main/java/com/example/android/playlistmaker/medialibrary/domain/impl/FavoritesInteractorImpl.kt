package com.example.android.playlistmaker.medialibrary.domain.impl

import com.example.android.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.example.android.playlistmaker.medialibrary.domain.api.FavoritesRepository
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {
    override suspend fun favoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.favoritesTracks()
    }
}