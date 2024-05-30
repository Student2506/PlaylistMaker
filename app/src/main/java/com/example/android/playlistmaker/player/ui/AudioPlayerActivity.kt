package com.example.android.playlistmaker.player.ui

import android.os.Bundle
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.player.presentation.PlayerPresenter
import com.example.android.playlistmaker.player.presentation.PlayerView
import com.example.android.playlistmaker.search.domain.models.Track
import com.example.android.playlistmaker.search.ui.SearchActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity(), PlayerView {

    private var playerPresenter: PlayerPresenter? = null

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
    private val header: FrameLayout by lazy { findViewById<FrameLayout>(R.id.flBackToMain) }

    private val timeFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        header.setOnClickListener {
            finish()
        }
        val track: Track = intent.getParcelableExtra(SearchActivity.TRACK_TO_SHOW)!!
        playerPresenter = Creator.providePlayerPresenter(this, track)
        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder).centerInside().transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ROUND_CORNERS_SIZE_PX,
                        applicationContext.resources.displayMetrics
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
        playerPresenter?.preparePlayer()
        playButton.setOnClickListener {
            playerPresenter?.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        playerPresenter?.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPresenter?.onDestroy()
    }

    override fun changePlayButton(isPlay: Boolean) {
        playButton.isEnabled = true
        if (isPlay) {
            playButton.setImageResource(R.drawable.play)
        } else {
            playButton.setImageResource(R.drawable.pause)
        }
    }

    override fun updateElapsedTime(time: Long) {
        elapsedTime.text = timeFormatter.format(time)
    }

    companion object {
        const val ROUND_CORNERS_SIZE_PX = 8f
        private const val TAG = "AudioPlayerActivity"
    }
}