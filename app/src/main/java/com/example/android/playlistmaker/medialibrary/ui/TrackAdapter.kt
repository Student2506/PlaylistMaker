package com.example.android.playlistmaker.medialibrary.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.medialibrary.domain.models.Track

class TrackAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {
    private val tracks = ArrayList<Track>()

    fun updateRecycleView(tracks: ArrayList<Track>) {
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
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}