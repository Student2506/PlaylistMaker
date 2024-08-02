package com.example.android.playlistmaker.player.data

import com.example.android.playlistmaker.player.data.converters.TrackConverter
import com.example.android.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.util.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioPlayerRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackConverter: TrackConverter,
    private val playerClient: PlayerClient,
) : AudioPlayerRepository {

    override fun controlPlayer(command: Command): Flow<State> = flow {
        emit(playerClient.doRequest(command))
    }

    override fun getTime(): Flow<State> = flow {
        emit(playerClient.getTime())
    }

    override fun favoriteTrack(id: Long): Flow<Boolean> = flow {
        val is_favorite = appDatabase.favoriteTracksDao().getFavoriteTrack(id) > 0
        emit(is_favorite)
    }

    override fun switchTrackFavorite(track: Track): Flow<Boolean> = flow {
        val is_favorite = appDatabase.favoriteTracksDao().getFavoriteTrack(track.trackId) > 0
        if (is_favorite) {
            appDatabase.favoriteTracksDao().removeTrack(trackConverter.map(track))
            val isInDB = appDatabase.favoriteTracksDao().getFavoriteTrack(track.trackId) > 0
            emit(isInDB)
        } else {
            appDatabase.favoriteTracksDao().insertTrack(trackConverter.map(track))
            val isInDB = appDatabase.favoriteTracksDao().getFavoriteTrack(track.trackId) > 0
            emit(isInDB)
        }
    }
}