package com.example.android.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Start search activity!", Toast.LENGTH_LONG).show()
            }
        }
        searchButton.setOnClickListener(searchClickListener)
        val settingsButton = findViewById<Button>(R.id.settings)
        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Start settings activity", Toast.LENGTH_SHORT).show()
        }
        val mediaLibraryButton = findViewById<Button>(R.id.media_library)
        mediaLibraryButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Open Media Library!", Toast.LENGTH_LONG).show()
        }
    }
}