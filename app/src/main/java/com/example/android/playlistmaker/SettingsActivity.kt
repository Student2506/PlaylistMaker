package com.example.android.playlistmaker

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val header = findViewById<FrameLayout>(R.id.back_to_main)
        header.setOnClickListener {
            finish()
        }
    }
}