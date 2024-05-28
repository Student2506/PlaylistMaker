package com.example.android.playlistmaker.player.data.dto

sealed interface CommandDto {

    object Pause : CommandDto

    object Play : CommandDto

    data class Prepare(val trackUrl: String) : CommandDto

    object PlayPause : CommandDto

    object Release : CommandDto
}