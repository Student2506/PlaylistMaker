package com.example.android.playlistmaker.domain.impl

import android.widget.ImageView
import com.example.android.playlistmaker.domain.api.TrackImageInteractor
import com.example.android.playlistmaker.domain.api.TrackImageRepository
import java.util.concurrent.Executors

class TrackImageInteractorImpl(private val repository: TrackImageRepository) :
    TrackImageInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchImage(url: String, imageView: ImageView) {
        executor.execute {
            repository.searchImage(url, imageView)
        }
    }
}