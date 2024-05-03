package com.example.android.playlistmaker.domain.impl

import com.example.android.playlistmaker.domain.api.TracksInteractor
import com.example.android.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val respository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(term: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(respository.searchTracks(term))
        }
    }
}