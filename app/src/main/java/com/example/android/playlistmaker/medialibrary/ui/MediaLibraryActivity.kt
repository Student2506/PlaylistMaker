package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.android.playlistmaker.medialibrary.presentation.MediaLibraryPagerAdapter
import com.example.android.playlistmaker.medialibrary.presentation.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private val binding: ActivityMediaLibraryBinding by lazy {
        ActivityMediaLibraryBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: MediaLibraryViewModel by viewModels()
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vpMediaLib.adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tlMediaLib, binding.vpMediaLib) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
        binding.flBackToMainSettings.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    companion object {
        private val TAG = "MediaLibraryActivity"
    }
}