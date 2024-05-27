package com.example.android.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator


class AudioPlayerActivity : AppCompatActivity() {

    private val playerController = Creator.providePlayerController(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        playerController.onCreate()
    }

    override fun onPause() {
        super.onPause()
        playerController.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerController.onDestroy()
    }

    companion object {
        const val ROUND_CORNERS_SIZE_PX = 8f
        private const val TAG = "AudioPlayerActivity"
    }
}