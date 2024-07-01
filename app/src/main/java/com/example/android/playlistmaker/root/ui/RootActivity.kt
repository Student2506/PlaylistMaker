package com.example.android.playlistmaker.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.ActivityRootBinding
import com.example.android.playlistmaker.medialibrary.ui.MediaLibraryFragment
import com.example.android.playlistmaker.search.ui.SearchFragment

class RootActivity : AppCompatActivity() {

    private var binding: ActivityRootBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding?.bottomNavigationView?.setupWithNavController(navController)
//        if (savedInstanceState == null) {
//            supportFragmentManager.commit {
//                this.add(R.id.rootFragmentContainerView, MediaLibraryFragment())
//            }
//        }
    }
}