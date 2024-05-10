package com.example.android.playlistmaker.data.player

interface MediaPlayerClient {
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl(): STATE
    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit, onCompletePlay: () -> Unit)
    fun release()
    fun trackPlayed(): Int

    enum class STATE {
        STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED
    }

}