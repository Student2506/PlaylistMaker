package com.example.android.playlistmaker

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.SearchActivity.Companion.TRACK_TO_SHOW
import com.example.android.playlistmaker.datalayer.Track
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val header = findViewById<FrameLayout>(R.id.flBackToMain)
        header.setOnClickListener {
            finish()
        }
        val track: Track = intent.getParcelableExtra(TRACK_TO_SHOW)!!
        Log.d("AudioPlayer", track.toString())
        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerInside()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ROUND_CORNERS_SIZE_PX,
                        applicationContext.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(trackImage)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDurationTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
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
    }

    companion object {
        private const val ROUND_CORNERS_SIZE_PX = 8f
    }
}