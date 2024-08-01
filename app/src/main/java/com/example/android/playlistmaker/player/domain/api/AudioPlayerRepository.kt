package com.example.android.playlistmaker.player.domain.api

import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {
    fun controlPlayer(command: Command): Flow<State>
    fun getTime(): Flow<State>
    fun favoriteTrack(id: Long): Flow<Boolean>
    fun switchTrackFavorite(track: Track): Flow<Boolean>
}