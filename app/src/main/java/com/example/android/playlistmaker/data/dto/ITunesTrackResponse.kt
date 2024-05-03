package com.example.android.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

class ITunesTrackResponse(@SerializedName("results") val tracks: List<TrackDto>) : Response()