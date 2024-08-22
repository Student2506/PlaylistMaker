package com.example.android.playlistmaker.player.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.android.playlistmaker.player.domain.models.Playlist
import com.example.android.playlistmaker.player.domain.models.State
import com.example.android.playlistmaker.player.domain.models.Track
import com.example.android.playlistmaker.player.presentation.AdditionStatus
import com.example.android.playlistmaker.player.presentation.PlayerViewModel
import com.example.android.playlistmaker.player.presentation.PlaylistState
import com.example.android.playlistmaker.util.extensions.showCustomToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        bottomSheetBehavior = BottomSheetBehavior.from(binding?.standardBottomSheet!!)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding?.overlay?.isVisible = false
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        binding?.overlay?.isVisible = true
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
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
        binding?.ivAddTrackToPlaylist?.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        playerViewModel.observePlaylists().observe(this) { state ->
            when (state) {
                is PlaylistState.Content -> showContent(state.playlist)
                is PlaylistState.Empty -> showEmpty()
            }

        }
        playerViewModel.observeToastLiveData().observe(this) { state ->
            when (state) {
                is AdditionStatus.Success -> {
                    Toast(this).showCustomToast(
                        getString(
                            R.string.add_to_playlist_success, state.playlistTitle
                        ), this
                    )
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }

                is AdditionStatus.Error -> {
                    Toast(this).showCustomToast(
                        getString(
                            R.string.track_already_in_playlist, state.playlistTitle
                        ), this
                    )
                }
            }
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

    private fun showContent(playlists: List<Playlist>) {
        val adapter = TrackAdapter {
            playerViewModel.addTrackToPlaylist(it)
        }
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        binding?.rvPlaylist?.layoutManager = LinearLayoutManager(this)
        binding?.rvPlaylist?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        val adapter = TrackAdapter {}
        adapter.playlists.clear()
        binding?.rvPlaylist?.layoutManager = LinearLayoutManager(this)
        binding?.rvPlaylist?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val ROUND_CORNERS_SIZE_PX = 8f
        const val TRACK_TO_SHOW = "track_to_show"
        private const val TAG = "AudioPlayerActivity"
    }
}