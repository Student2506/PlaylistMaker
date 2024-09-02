package com.example.android.playlistmaker.medialibrary.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.medialibrary.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : CreatePlaylistFragment(false) {

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist_id"
        private const val TAG = "EditPlaylistFragment"
        private const val ROUND_CORNERS_SIZE_PX = 8f

        fun createArgs(playlistId: Long): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }

    private val playlistId by lazy { requireArguments().getLong(ARGS_PLAYLIST_ID) }

    private val viewModel: EditPlaylistViewModel by viewModel {
        parametersOf(playlistId)
    }

    private var cover: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressedCallback.isEnabled = false
        binding.bCreatePlaylist.text = getString(R.string.save_button)
        binding.tbToolbar.title = getString(R.string.edit_title)

        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlist ->
            binding.tietPlaylistTitle.setText(playlist.title)
            binding.tietPlaylistDescription.setText(playlist.description ?: "")
            Log.d(TAG, "My image ${playlist.imageUrl}")
            binding.ivPlaylistCover.setPadding(0, 0, 0, 0)
            Glide.with(requireContext()).load(playlist.imageUrl).transform(
                CenterCrop(), RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ROUND_CORNERS_SIZE_PX,
                        requireContext().resources.displayMetrics
                    ).toInt()
                )
            ).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .placeholder(R.drawable.placeholder_no_cover).into(binding.ivPlaylistCover)
        }
        binding.tbToolbar.setOnClickListener {
            onBackPressedCallback.isEnabled = true
            findNavController().navigateUp()
        }

        binding.bCreatePlaylist.setOnClickListener {
            viewModel.savePlaylist(
                binding.tietPlaylistTitle.text.toString(),
                binding.tietPlaylistDescription.text.toString(),
                cover.toString()
            )
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedCallback.isEnabled = true
                findNavController().navigateUp()
            }
        })
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.ivPlaylistCover.setPadding(0, 0, 0, 0)
                    Glide.with(requireContext()).load(uri).transform(
                        CenterCrop(), RoundedCorners(
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                ROUND_CORNERS_SIZE_PX,
                                requireContext().resources.displayMetrics
                            ).toInt()
                        )
                    ).into(binding.ivPlaylistCover)
                    cover = viewModel.chooseFileCopyToCache(uri)
                } else {
                    Log.d(TAG, "No media selected")
                }
            }

        val requestPremissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                }
            }

        binding.ivPlaylistCover.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionGranted = ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_MEDIA_IMAGES
                )
                if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
                    requestPremissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                val permissionGranted = ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
                    requestPremissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }
}