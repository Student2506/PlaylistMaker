package com.example.android.playlistmaker.medialibrary.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist

class PlaylistTileAdapter(
    private val playlists: List<Playlist>,
    private val clickListener: PlaylistClickListener,
) : RecyclerView.Adapter<PlaylistTileVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTileVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_tile, parent, false)
        return PlaylistTileVH(view)
    }

    override fun onBindViewHolder(holder: PlaylistTileVH, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistClcik(playlists[position]) }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface PlaylistClickListener {
        fun onPlaylistClcik(playlist: Playlist)
    }
}