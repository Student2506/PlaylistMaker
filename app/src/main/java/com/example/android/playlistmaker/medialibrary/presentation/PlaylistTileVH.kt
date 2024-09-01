package com.example.android.playlistmaker.medialibrary.presentation

import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.ui.ModifiedImageView

class PlaylistTileVH(private val view: View) : RecyclerView.ViewHolder(view) {

    private val cover: ModifiedImageView = itemView.findViewById(R.id.mivPlaylistCover)
    private val title: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
    private val trackQty: TextView = itemView.findViewById(R.id.tvPlaylistTrackQty)

    fun bind(playlist: Playlist) {

        title.text = playlist.title
        trackQty.text = TrackCount(playlist.tracks?.size ?: 0)
        if (!playlist.imageUrl.isNullOrEmpty()) {
            Glide.with(itemView.context).load(playlist.imageUrl)
                .placeholder(R.drawable.placeholder_no_cover)
                .fallback(R.drawable.placeholder_no_cover).transform(
                    CenterCrop(), RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            ROUND_CORNERS_SIZE_PX,
                            view.context.resources.displayMetrics
                        ).toInt()
                    )
                ).into(cover)
        } else {
            cover.setImageResource(R.drawable.placeholder_no_cover)
        }

    }

    private fun TrackCount(trackQty: Int): String {
        if (trackQty % 100 in 5..20) return "$trackQty треков"
        if (trackQty % 10 == 1) return "$trackQty трек"
        if (trackQty % 10 in 2..4) return "$trackQty трека"
        else return "$trackQty треков"
    }

    companion object {
        private const val ROUND_CORNERS_SIZE_PX = 8f
        private const val TAG = "PlaylistTileVH"
    }
}