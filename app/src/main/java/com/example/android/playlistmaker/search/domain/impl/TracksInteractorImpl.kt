package com.example.android.playlistmaker.search.domain.impl

import com.example.android.playlistmaker.search.domain.api.TracksInteractor
import com.example.android.playlistmaker.search.domain.api.TracksRepository
import com.example.android.playlistmaker.search.domain.models.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val respository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(term: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = respository.searchTracks(term)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }

        }
    }
}