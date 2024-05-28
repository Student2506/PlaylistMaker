package com.example.android.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class PlayerPresenter(
    private val view: PlayerView,
    private val track: Track,
) {

    private val playerInteractor = Creator.provideAudioPlayerInteractor()

    private val handler = Handler(Looper.getMainLooper())

    private val timeFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        )
    }
    private val trackTime: Runnable by lazy {
        object : Runnable {
            override fun run() {
                playerInteractor.getTrackTime(object :
                    AudioPlayerInteractor.AudioPlayerTrackTimeConsumer {
                    override fun getTime(time: Int) {
                        handler.post {
                            view.updateTrackTime(timeFormatter.format(time))
                        }
                    }
                })
                handler.postDelayed(this, REFRESH_TRACK_DELAY_MILLIS)
            }
        }
    }

    fun onCreate() {

        val instant = Instant.parse(track.releaseDate)
        val zone = ZoneId.of("Z")
        val date = instant.atZone(zone).toLocalDate()

        view.setupTextElements(track)
        view.enableTrackPlay(track.previewUrl != null)
        view.setTime(
            timeFormatter.format(track.trackTime), timeFormatter.format(0L)
        )
        view.setYear(date.year.toString())
        view.setupPosterImage(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
    }

    fun loadPlayer() {
        playerInteractor.preparePlayer(
            track.previewUrl!!,
            object : AudioPlayerInteractor.AudioPlayerConsumer {
                override fun consume(status: State) {
                    Log.d(TAG, status.toString())
                }
            })
    }

    fun onDestroy() {
        releasePlayer()
        handler.removeCallbacks(trackTime)
    }


    private fun startPlayer() {
        playerInteractor.startPlayer(object : AudioPlayerInteractor.AudioPlayerConsumer {
            override fun consume(status: State) {
                handler.post {
                    view.updateButtonPlayPause(isPlay = true)
                    handler.postDelayed(trackTime, REFRESH_TRACK_DELAY_MILLIS)
                }
            }
        })
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer(object : AudioPlayerInteractor.AudioPlayerConsumer {
            override fun consume(status: State) {
                handler.post {
                    view.updateButtonPlayPause(isPlay = false)
                }
            }
        })
        handler.removeCallbacks(trackTime)
    }

    fun releasePlayer() {
        playerInteractor.release(object : AudioPlayerInteractor.AudioPlayerConsumer {
            override fun consume(status: State) {
                Log.d(TAG, "Released")
            }
        })
    }

    fun playbackControl() {
        playerInteractor.playbackControl(object : AudioPlayerInteractor.AudioPlayerConsumer {
            override fun consume(status: State) {
                handler.post {
                    view.updateButtonPlayPause(status == State.STATE_PLAYING)
                }
            }
        })
    }

    companion object {
        private const val REFRESH_TRACK_DELAY_MILLIS = 400L  // 2-3 time a second
        private const val TAG = "PlayerController"
    }

}