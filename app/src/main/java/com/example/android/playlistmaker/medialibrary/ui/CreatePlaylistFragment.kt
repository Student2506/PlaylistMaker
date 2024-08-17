package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.android.playlistmaker.medialibrary.presentation.CreatePlaylistViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment

class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = CreatePlaylistFragment()

        private const val TAG = "CreatePlaylistFragment"
    }

//    private val viewModel: CreatePlaylistViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCreatePlaylistBinding =
        FragmentCreatePlaylistBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}