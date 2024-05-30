package com.example.android.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.models.Command
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.search.domain.models.Track

class PlayerPresenter(
    private val view: PlayerView,
    private val track: Track,
) {

    private val playerInteractor = Creator.provideAudioPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())


    private val trackTime: Runnable by lazy {
        object : Runnable {
            override fun run() {
                playerInteractor.getTrackTime(object :
                    AudioPlayerInteractor.AudioPlayerTrackTimeConsumer {
                    override fun getTime(time: Long) {
                        handler.post {
                            view.updateElapsedTime(time)
                        }
                    }
                })
                handler.postDelayed(this, REFRESH_TRACK_DELAY_MILLIS)
            }
        }
    }

    fun preparePlayer() {
        if (track.previewUrl != null) {
            playerInteractor.controlPlayer(
                Command.Prepare(track.previewUrl),
                object : AudioPlayerInteractor.AudioPlayerConsumer {
                    override fun consume(status: State) {
                        handler.post {
                            view.render(PlayerState.Loading)
                            view.updateElapsedTime(0L)
                        }
                    }
                })
        }
    }

    fun onDestroy() {
        releasePlayer()
        handler.removeCallbacks(trackTime)
    }

    private fun startPlayer() {
        playerInteractor.controlPlayer(Command.Play,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    handler.post {
                        view.render(PlayerState.Content(true))
                        handler.postDelayed(trackTime, REFRESH_TRACK_DELAY_MILLIS)
                    }
                }
            })
    }

    fun pausePlayer() {
        playerInteractor.controlPlayer(Command.Pause,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    handler.post {
                        view.render(PlayerState.Content(true))
                    }
                }
            })
        handler.removeCallbacks(trackTime)
    }

    fun releasePlayer() {
        playerInteractor.controlPlayer(Command.Release,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    Log.d(TAG, "Released")
                }
            })
    }

    fun playbackControl() {
        playerInteractor.controlPlayer(Command.PlayPause,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    if (status == State.STATE_PLAYING) {
                        handler.post {
                            view.render(PlayerState.Content(false))
                        }
                    } else {
                        handler.post {
                            view.render(PlayerState.Content(true))
                        }
                    }
                }
            })
    }

    companion object {
        private const val REFRESH_TRACK_DELAY_MILLIS = 400L  // 2-3 time a second
        private const val TAG = "PlayerController"
    }

}