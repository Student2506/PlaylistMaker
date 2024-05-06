package com.example.android.playlistmaker.domain.api

import android.widget.ImageView

interface TrackImageRepository {
    fun searchImage(url: String, iv: ImageView)
}