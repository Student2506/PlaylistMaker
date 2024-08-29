package com.example.android.playlistmaker.player.domain.models

sealed interface Status {
    object Succses : Status
    object ExistFailure : Status
}
