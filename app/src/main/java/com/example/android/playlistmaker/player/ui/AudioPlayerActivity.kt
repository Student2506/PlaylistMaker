package com.example.android.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track: Track = intent.getParcelableExtra(SearchActivity.TRACK_TO_SHOW)!!
        playerPresenter = Creator.providePlayerPresenter(this, track)
        playButton.setOnClickListener {
            playerPresenter?.playbackControl()
        }
        header.setOnClickListener {
            finish()
        }
        playerPresenter?.onCreate()
    }

    override fun onPause() {
        super.onPause()
        playerPresenter?.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPresenter?.onDestroy()
    }

    override fun setupPosterImage(imageUrl: String) {
        Log.d(TAG, "Load poster")
        Glide.with(applicationContext)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder).centerInside().transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ROUND_CORNERS_SIZE_PX,
                        applicationContext.resources.displayMetrics
                    ).toInt()
                )
            ).into(trackImage)
    }

    override fun enableCollectionName(isVisible: Boolean) {
        albumName.isVisible = isVisible
        albumNameHeader.isVisible = isVisible
    }

    override fun setupCollectionName(collectionName: String?) {
        if (collectionName != null) {
            albumName.text = collectionName
            enableCollectionName(true)
        } else enableCollectionName(false)
    }

    override fun setupTextElements(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        genre.text = track.primaryGenreName
        country.text = track.country
    }

    override fun enableTrackPlay(isVisible: Boolean) {
        playButton.isEnabled = isVisible
        if (isVisible) {
            playerPresenter?.loadPlayer()
            Log.d(TAG, "Load button")
            playButton.setImageResource(R.drawable.play)

        }
    }

    override fun setTime(trackDuration: String, trackPlayed: String) {
        trackDurationTime.text = trackDuration
        elapsedTime.text = trackPlayed
    }

    override fun setYear(yearTrack: String) {
        year.text = yearTrack
    }

    override fun updateTrackTime(time: String) {
        elapsedTime.text = time
    }

    override fun updateButtonPlayPause(isPlay: Boolean) {
        Log.d(TAG, "Load button 2")
        if (isPlay) playButton.setImageResource(R.drawable.pause)
        else playButton.setImageResource(R.drawable.play)
    }

    companion object {
        const val ROUND_CORNERS_SIZE_PX = 8f
        private const val TAG = "AudioPlayerActivity"
    }
}