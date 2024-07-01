package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.example.android.playlistmaker.medialibrary.presentation.MediaLibraryPagerAdapter
import com.example.android.playlistmaker.medialibrary.presentation.MediaLibraryViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment : BindingFragment<FragmentMediaLibraryBinding>() {

    private val viewModel: MediaLibraryViewModel by viewModels()
    private lateinit var tabMediator: TabLayoutMediator

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMediaLibraryBinding = FragmentMediaLibraryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpMediaLib.adapter = MediaLibraryPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tlMediaLib, binding.vpMediaLib) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
//        binding.tbToolbar.setOnClickListener {
//            finish()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MediaLibraryFragment()
        private val TAG = "MediaLibraryActivity"
    }
}