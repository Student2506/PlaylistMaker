package com.example.android.playlistmaker.datalayer

import com.google.gson.annotations.SerializedName

class ITunesTrackResponse (@SerializedName("results") val tracks: ArrayList<Track>)