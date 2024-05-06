package com.example.android.playlistmaker.data

import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.domain.api.TrackImageRepository
import com.example.android.playlistmaker.presentation.ui.audioplayer.AudioPlayerActivity

class TrackImageRepositoryImpl() : TrackImageRepository {

    override fun searchImage(url: String, iv: ImageView) {
        iv.post {
            Glide.with(iv.context.applicationContext)
                .load(url.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerInside()
                .transform(
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            AudioPlayerActivity.ROUND_CORNERS_SIZE_PX,
                            iv.context.applicationContext.resources.displayMetrics
                        ).toInt()
                    )
                )
                .into(iv)
        }
    }
}