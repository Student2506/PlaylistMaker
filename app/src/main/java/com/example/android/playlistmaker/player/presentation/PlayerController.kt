package com.example.android.playlistmaker.player.presentation

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.ui.AudioPlayerActivity
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.search.ui.SearchActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class PlayerController(
    private val activity: Activity,
) {

    private val playerInteractor = Creator.provideAudioPlayerInteractor()
    private val trackImage: ImageView by lazy { activity.findViewById(R.id.ivTrackImage) }
    private val trackName: TextView by lazy { activity.findViewById(R.id.tvTrackName) }
    private val artistName: TextView by lazy { activity.findViewById(R.id.tvArtistName) }
    private val trackDurationTime: TextView by lazy { activity.findViewById(R.id.tvTrackDurationTime) }
    private val albumName: TextView by lazy { activity.findViewById(R.id.tvAlbumName) }
    private val albumNameHeader: TextView by lazy { activity.findViewById(R.id.tvAlbum) }
    private val year: TextView by lazy { activity.findViewById(R.id.tvYearValue) }
    private val genre: TextView by lazy { activity.findViewById(R.id.tvGenreValue) }
    private val country: TextView by lazy { activity.findViewById(R.id.tvCountryValue) }
    private val playButton: ImageButton by lazy { activity.findViewById(R.id.ibPlayButton) }
    private val elapsedTime: TextView by lazy { activity.findViewById(R.id.tvTrackElapsed) }
    private val header: FrameLayout by lazy { activity.findViewById<FrameLayout>(R.id.flBackToMain) }
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
                            elapsedTime.text = timeFormatter.format(time)
                        }
                    }
                })
                handler.postDelayed(this, REFRESH_TRACK_DELAY_MILLIS)
            }
        }
    }

    fun onCreate() {
        header.setOnClickListener {
            activity.finish()
        }
        val track: Track = activity.intent.getParcelableExtra(SearchActivity.TRACK_TO_SHOW)!!
        Glide.with(activity.applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder).centerInside().transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        AudioPlayerActivity.ROUND_CORNERS_SIZE_PX,
                        activity.applicationContext.resources.displayMetrics
                    ).toInt()
                )
            ).into(trackImage)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDurationTime.text = timeFormatter.format(track.trackTime)
        if (track.collectionName.isNotEmpty()) albumName.text = track.collectionName
        else {
            albumName.isVisible = false
            albumNameHeader.isVisible = false
        }
        val instant = Instant.parse(track.releaseDate)
        val zone = ZoneId.of("Z")
        val date = instant.atZone(zone).toLocalDate()
        year.text = date.year.toString()
        genre.text = track.primaryGenreName
        country.text = track.country
        playButton.isEnabled = false
        if (track.previewUrl != null) {
            playerInteractor.preparePlayer(
                track.previewUrl,
                object : AudioPlayerInteractor.AudioPlayerConsumer {
                    override fun consume(status: State) {
                        handler.post {
                            playButton.isEnabled = true
                            playButton.setImageResource(R.drawable.play)
                            elapsedTime.text = timeFormatter.format(0L)
                        }
                    }
                })
        }
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    fun onDestroy() {
        handler.removeCallbacks(trackTime)
    }

    private fun startPlayer() {
        playerInteractor.startPlayer(object : AudioPlayerInteractor.AudioPlayerConsumer {
            override fun consume(status: State) {
                handler.post {
                    playButton.setImageResource(R.drawable.pause)
                    handler.postDelayed(trackTime, REFRESH_TRACK_DELAY_MILLIS)
                }
            }
        })
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer(object : AudioPlayerInteractor.AudioPlayerConsumer {
            override fun consume(status: State) {
                handler.post {
                    playButton.setImageResource(R.drawable.play)
                }
            }
        })
        handler.removeCallbacks(trackTime)
    }

    private fun playbackControl() {
        playerInteractor.playbackControl(object : AudioPlayerInteractor.AudioPlayerConsumer {
            override fun consume(status: State) {
                if (status == State.STATE_PLAYING) {
                    handler.post {
                        playButton.setImageResource(R.drawable.pause)
                    }
                } else {
                    handler.post {
                        playButton.setImageResource(R.drawable.play)
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