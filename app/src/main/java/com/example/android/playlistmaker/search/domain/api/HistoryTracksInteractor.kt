package com.example.android.playlistmaker.search.domain.api

interface HistoryTracksInteractor {
    fun putHistoryTracks(historyTracks: String, consumer: HistoryTracksConsumer)
    fun getHistoryTracks(consumer: HistoryTracksConsumer)

    interface HistoryTracksConsumer {
        fun consume(result: Any)
    }
}