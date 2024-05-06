package com.example.android.playlistmaker.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.presentation.ui.general_settings.SettingsActivity
import com.example.android.playlistmaker.presentation.ui.medialibrary.MediaLibraryActivity
import com.example.android.playlistmaker.presentation.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)
        val searchClickListener: View.OnClickListener = View.OnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        searchButton.setOnClickListener(searchClickListener)
        val settingsButton = findViewById<Button>(R.id.settings)
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
        val mediaLibraryButton = findViewById<Button>(R.id.media_library)
        mediaLibraryButton.setOnClickListener {
            val mediaLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaLibraryIntent)
        }
    }
}