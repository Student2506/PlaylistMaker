package com.example.android.playlistmaker.player.domain.models

sealed class State(val isPlayButtonEnabled: Boolean, val buttonState: ButtonState) {
    object Default : State(false, ButtonState.Play)
    object Playing : State(true, ButtonState.Pause)
    object Paused : State(true, ButtonState.Play)
    object Prepared : State(true, ButtonState.Play)

    sealed interface ButtonState {
        object Play : ButtonState
        object Pause : ButtonState
    }
}