package com.example.android.playlistmaker.search.domain.api

interface HistoryTracksRepository {
    fun putHistoryTracks(term: String, value: String): Int
    fun getHistoryTracks(term: String): String
}