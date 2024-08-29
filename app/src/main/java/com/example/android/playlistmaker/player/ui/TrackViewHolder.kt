package com.example.android.playlistmaker.player.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.player.domain.models.Playlist

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.playlist_element, parent, false)
) {

    private val playlistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
    private val tvTrackQty: TextView = itemView.findViewById(R.id.tvTrackQty)

    fun bind(model: Playlist) {
        playlistTitle.text = model.title
        tvTrackQty.text = TrackCount(model.tracks?.size ?: 0)
        val urlCover = itemView.findViewById<ImageView>(R.id.ivPlaylist)
        Glide.with(itemView.context).load(model.imageUrl).placeholder(R.drawable.placeholder)
            .centerInside().transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ROUND_CORNERS_SIZE_PX,
                        itemView.context.resources.displayMetrics
                    ).toInt()
                )
            ).into(urlCover)
    }

    private fun TrackCount(trackQty: Int): String {
        if (trackQty % 100 in 5..20) return "$trackQty треков"
        if (trackQty % 10 == 1) return "$trackQty трек"
        if (trackQty % 10 in 2..4) return "$trackQty трека"
        else return "$trackQty треков"
    }

    companion object {
        private const val ROUND_CORNERS_SIZE_PX = 2f
    }
}