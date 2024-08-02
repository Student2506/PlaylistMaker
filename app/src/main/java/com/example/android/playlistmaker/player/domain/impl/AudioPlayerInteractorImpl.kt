package com.example.android.playlistmaker.player.domain.impl

import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class AudioPlayerInteractorImpl(private val player: AudioPlayerRepository) : AudioPlayerInteractor {

    override fun controlPlayer(request: Command): Flow<State> {
        return player.controlPlayer(request)
    }

    override fun getTrackTime(): Flow<State> {
        return player.getTime()
    }

    override fun favoriteTrack(id: Long): Flow<Boolean> {
        return player.favoriteTrack(id)
    }

    override fun switchFavorite(track: Track): Flow<Boolean> {
        return player.switchTrackFavorite(track)
    }
}