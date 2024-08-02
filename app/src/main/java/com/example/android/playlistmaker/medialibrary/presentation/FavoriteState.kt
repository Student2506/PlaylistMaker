package com.example.android.playlistmaker.medialibrary.presentation

import com.example.android.playlistmaker.medialibrary.domain.models.Track

sealed interface FavoriteState {

    object Loading : FavoriteState

    data class Content(val tracks: List<Track>) : FavoriteState

    object Empty : FavoriteState
}