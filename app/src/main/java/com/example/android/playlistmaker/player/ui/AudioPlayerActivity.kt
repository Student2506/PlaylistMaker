package com.example.android.playlistmaker.player.ui

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.player.presentation.PlayerViewModel
import com.example.android.playlistmaker.util.TrackConverter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private val track: Track by lazy {
        intent.getParcelableExtra(TRACK_TO_SHOW)!!
    }
    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }
    private var binding: ActivityAudioPlayerBinding? = null

    private val timeFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.tbToolbar?.setOnClickListener {
            finish()
        }
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
            ).into(binding?.ivTrackImage!!)
        binding?.tvTrackName?.text = track.trackName
        binding?.tvArtistName?.text = track.artistName
        binding?.tvTrackDurationTime?.text = timeFormatter.format(track.trackTime)
        if (track.collectionName.isNotEmpty()) binding?.tvAlbumName?.text = track.collectionName
        else {
            binding?.tvAlbumName?.isVisible = false
            binding?.tvAlbum?.isVisible = false
        }
        val instant = Instant.parse(track.releaseDate)
        val zone = ZoneId.of("Z")
        val date = instant.atZone(zone).toLocalDate()
        binding?.tvYearValue?.text = date.year.toString()
        binding?.tvGenreValue?.text = track.primaryGenreName
        binding?.tvCountryValue?.text = track.country
        binding?.ibPlayButton?.isEnabled = false
        playerViewModel.preparePlayer()

        binding?.ibPlayButton?.setOnClickListener {
            playerViewModel.playbackControl()
        }
        playerViewModel.observeState().observe(this) { state ->
            updatePlayButton(state)
        }
        playerViewModel.observeTrackState().observe(this) {
            updateElapsedTime(it)
        }
        playerViewModel.observeFavoriteState().observe(this) {
            val imageResource = if (it) R.drawable.like_done else R.drawable.like
            binding?.ivLikeButton?.setImageResource(imageResource)
        }
        binding?.ivLikeButton?.setOnClickListener {
            playerViewModel.updateFavorite()
        }
    }

    private fun updateElapsedTime(time: Int) {
        binding?.tvTrackElapsed?.text = timeFormatter.format(time)
    }

    private fun updatePlayButton(state: State) {
        binding?.ibPlayButton?.isEnabled = state.isPlayButtonEnabled
        if (state.buttonState is State.ButtonState.Play) {
            binding?.ibPlayButton?.setImageResource(R.drawable.play)
        } else {
            binding?.ibPlayButton?.setImageResource(R.drawable.pause)
        }

    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    companion object {
        const val ROUND_CORNERS_SIZE_PX = 8f
        const val TRACK_TO_SHOW = "track_to_show"
        private const val TAG = "AudioPlayerActivity"
    }
}