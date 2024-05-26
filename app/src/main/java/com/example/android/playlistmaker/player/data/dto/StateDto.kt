package com.example.android.playlistmaker.player.data.dto

sealed interface StateDto {
    object Default : StateDto
    object Playing : StateDto
    object Paused : StateDto
    object Prepared : StateDto
}