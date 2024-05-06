package com.example.android.playlistmaker.presentation.ui.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.android.playlistmaker.Creator
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.data.dto.TrackDto
import com.example.android.playlistmaker.presentation.ui.search.SearchActivity.Companion.TRACK_TO_SHOW
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private val trackImage: ImageView by lazy { findViewById(R.id.ivTrackImage) }
    private val trackName: TextView by lazy { findViewById(R.id.tvTrackName) }
    private val artistName: TextView by lazy { findViewById(R.id.tvArtistName) }
    private val trackDurationTime: TextView by lazy { findViewById(R.id.tvTrackDurationTime) }
    private val albumName: TextView by lazy { findViewById(R.id.tvAlbumName) }
    private val albumNameHeader: TextView by lazy { findViewById(R.id.tvAlbum) }
    private val year: TextView by lazy { findViewById(R.id.tvYearValue) }
    private val genre: TextView by lazy { findViewById(R.id.tvGenreValue) }
    private val country: TextView by lazy { findViewById(R.id.tvCountryValue) }
    private val playButton: ImageButton by lazy { findViewById(R.id.ibPlayButton) }
    private val elapsedTime: TextView by lazy { findViewById(R.id.tvTrackElapsed) }
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val timeFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }
    private val trackTime: Runnable by lazy {
        object : Runnable {
            override fun run() {
                elapsedTime.post {
                    elapsedTime.text = timeFormatter.format(mediaPlayer.currentPosition)
                }
                handler.postDelayed(
                    this,
                    REFRESH_TRACK_DELAY_MILLIS
                )
            }
        }
    }
    private val audioPlayer = Creator.provideAudioPlayer()
    private val trackImageInteractor = Creator.provideTrackImageInteracor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val header = findViewById<FrameLayout>(R.id.flBackToMain)
        header.setOnClickListener {
            finish()
        }
        val track: TrackDto = intent.getParcelableExtra(TRACK_TO_SHOW)!!
        Log.d("AudioPlayer", track.toString())
        trackImageInteractor.searchImage(track.artworkUrl100, trackImage)
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
            audioPlayer.preparePlayer(track.previewUrl, playButton, elapsedTime)
        }
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayer.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.release()
        handler.removeCallbacks(trackTime)
    }

    private fun startPlayer() {
        audioPlayer.startPlayer()
        playButton.setImageResource(R.drawable.pause)
        handler.postDelayed(
            trackTime,
            REFRESH_TRACK_DELAY_MILLIS
        )
    }

    private fun pausePlayer() {
        audioPlayer.pausePlayer()
        playButton.setImageResource(R.drawable.play)
        handler.removeCallbacks(trackTime)
    }

    private fun playbackControl() {
        if (audioPlayer.playbackControl()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    companion object {
        public const val ROUND_CORNERS_SIZE_PX = 8f
        private const val REFRESH_TRACK_DELAY_MILLIS = 400L  // 2-3 time a second
        private const val TAG = "AudioPlayerActivity"
    }
}