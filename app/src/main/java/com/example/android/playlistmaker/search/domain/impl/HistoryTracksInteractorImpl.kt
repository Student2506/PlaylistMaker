package com.example.android.playlistmaker.search.domain.impl

import com.example.android.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.example.android.playlistmaker.search.domain.api.HistoryTracksRepository
import java.util.concurrent.Executors

class HistoryTracksInteractorImpl(private val repository: HistoryTracksRepository) :
    HistoryTracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun putHistoryTracks(
        historyTracks: String,
        consumer: HistoryTracksInteractor.HistoryTracksConsumer,
    ) {
        executor.execute {
            consumer.consume(repository.putHistoryTracks(KEY_FOR_SEARCH_HISTORY, historyTracks))
        }
    }

    override fun getHistoryTracks(consumer: HistoryTracksInteractor.HistoryTracksConsumer) {
        executor.execute {
            consumer.consume(repository.getHistoryTracks(KEY_FOR_SEARCH_HISTORY))
        }
    }

    companion object {
        const val KEY_FOR_SEARCH_HISTORY = "key_for_search_activity"
    }
}