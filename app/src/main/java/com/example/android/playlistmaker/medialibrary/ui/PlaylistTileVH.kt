package com.example.android.playlistmaker.medialibrary.ui

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist

class PlaylistTileVH(view: View) : RecyclerView.ViewHolder(view) {

    private val cover: ModifiedImageView = itemView.findViewById(R.id.mivPlaylistCover)
    private val title: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
    private val trackQty: TextView = itemView.findViewById(R.id.tvPlaylistTrackQty)

    fun bind(playlist: Playlist) {
        Glide.with(itemView.context).load(playlist.imageUrl)
            .placeholder(R.drawable.placeholder_no_cover).centerInside().transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ROUND_CORNERS_SIZE_PX,
                        itemView.context.resources.displayMetrics
                    ).toInt()
                )
            ).into(cover)
        title.text = playlist.title
        trackQty.text = TrackCount(playlist.tracks?.size ?: 0)
    }

    private fun TrackCount(trackQty: Int): String {
        if (trackQty % 100 in 5..20) return "$trackQty треков"
        if (trackQty % 10 == 1) return "$trackQty трек"
        if (trackQty % 10 in 2..4) return "$trackQty трека"
        else return "$trackQty треков"
    }

    companion object {
        private const val ROUND_CORNERS_SIZE_PX = 8f
    }
}