package com.example.android.playlistmaker.player.domain.models

sealed class State(
    val isPlayButtonEnabled: Boolean,
    val buttonState: ButtonState,
    val progress: Int,
) {
    object Default : State(false, ButtonState.Play, 0)
    class Playing(progress: Int) : State(true, ButtonState.Pause, progress)
    class Paused(progress: Int) : State(true, ButtonState.Play, progress)
    object Prepared : State(true, ButtonState.Play, 0)

    sealed interface ButtonState {
        object Play : ButtonState
        object Pause : ButtonState
    }
}