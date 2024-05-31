package com.example.android.playlistmaker.player.data

import androidx.lifecycle.LiveData
import com.example.android.playlistmaker.player.data.dto.Response

interface PlayerClient {
    fun doRequest(dto: Any): Response
    fun getTrackTime(): LiveData<Int>
}