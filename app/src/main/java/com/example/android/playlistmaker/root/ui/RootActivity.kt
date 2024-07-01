package com.example.android.playlistmaker.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.ActivityRootBinding
import com.example.android.playlistmaker.settings.ui.SettingsFragment

class RootActivity : AppCompatActivity() {

    private var binding: ActivityRootBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.rootFragmentContainerView, SettingsFragment())
            }
        }
    }
}