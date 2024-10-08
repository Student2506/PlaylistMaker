package com.example.android.playlistmaker.medialibrary.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.medialibrary.domain.models.Track
import com.example.android.playlistmaker.medialibrary.ui.TracksCallback

class TrackAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {
    private val tracks = ArrayList<Track>()

    fun updateRecycleView(tracks: List<Track>) {
        val diffCallback = TracksCallback(this.tracks, tracks)
        val diffTracks = DiffUtil.calculateDiff(diffCallback)
        this.tracks.clear()
        this.tracks.addAll(tracks)
        diffTracks.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
        holder.itemView.setOnLongClickListener { clickListener.onTrackLongClick(tracks[position]) }
    }

    interface TrackClickListener {
        fun onTrackClick(track: Track)
        fun onTrackLongClick(track: Track): Boolean
    }
}