package com.example.android.playlistmaker.player.domain.models

sealed interface Command {

    object Pause : Command

    object Play : Command

    data class Prepare(val trackUrl: String) : Command

    object PlayPause : Command

    object Release : Command
}