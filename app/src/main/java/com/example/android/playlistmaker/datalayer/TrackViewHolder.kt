package com.example.android.playlistmaker.datalayer

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView

    init {
        trackName = itemView.findViewById(R.id.trackName)
        artistName = itemView.findViewById(R.id.artistName)
        trackTime = itemView.findViewById(R.id.trackDuration)
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        val urlCover = itemView.findViewById<ImageView>(R.id.trackImage)
        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerInside()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2F,
                        itemView.context.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(urlCover)
    }

}