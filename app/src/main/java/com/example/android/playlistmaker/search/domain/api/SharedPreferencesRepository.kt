package com.example.android.playlistmaker.search.domain.api

interface SharedPreferencesRepository {
    fun putValue(term: String, value: Any): Int
    fun getValue(term: String): Any
}