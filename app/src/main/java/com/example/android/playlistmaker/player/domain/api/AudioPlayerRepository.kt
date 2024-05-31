package com.example.android.playlistmaker.player.domain.api

import androidx.lifecycle.LiveData
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State

interface AudioPlayerRepository {
    fun controlPlayer(command: Command): State
    fun getTrackStatus(): LiveData<Int>
}