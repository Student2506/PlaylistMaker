package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface AudioPlayerInteractor {
    fun controlPlayer(request: Command): Flow<State>
    fun getTrackTime(): Flow<State>
    fun favoriteTrack(id: Long): Flow<Boolean>
    fun switchFavorite(track: Track): Flow<Boolean>
}