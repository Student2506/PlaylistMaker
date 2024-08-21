package com.example.android.playlistmaker.player.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.player.domain.models.Playlist
import com.example.android.playlistmaker.util.data.db.entity.PlaylistWithTracksEntity

class TrackAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(playlists[position]) }
    }

    fun interface PlaylistClickListener {
        fun onTrackClick(playlist: Playlist)
    }
}