package com.example.android.playlistmaker.domain.api

import android.widget.ImageView

interface TrackImageInteractor {

    fun searchImage(url: String, imageView: ImageView)

}