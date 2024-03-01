package com.example.android.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.datalayer.Track
import com.example.android.playlistmaker.datalayer.TrackViewHolder

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }
}