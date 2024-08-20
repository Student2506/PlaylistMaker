package com.example.android.playlistmaker.medialibrary.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.android.playlistmaker.medialibrary.presentation.CreatePlaylistViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = CreatePlaylistFragment()

        const val TAG = "CreatePlaylistFragment"
        private const val ROUND_CORNERS_SIZE_PX = 8f
    }

    private val viewModel by viewModel<CreatePlaylistViewModel>()

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
        binding.tbToolbar.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tietPlaylistTitle.addTextChangedListener(beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                binding.bCreatePlaylist.isEnabled = !charSequence.isNullOrEmpty()
            },
            afterTextChanged = { _ -> })

        viewModel.observeState().observe(viewLifecycleOwner) { uri ->
            binding.ivPlaylistCover.setImageURI(uri)
        }

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
                    viewModel.chooseFileCopyToCache(uri)
                } else {
                    Log.d(TAG, "No media selected")
                }
            }

        val requestPremissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Log.d(TAG, "Permission Granted")
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    Log.d(TAG, "No permission")
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
        binding.bCreatePlaylist.setOnClickListener {
            Log.d(TAG, "create playlist button")

            viewModel.savePlaylist(
                binding.tietPlaylistTitle.text.toString(),
                binding.tietPlaylistDescription.text.toString()
            )
            viewModel.observeToastLiveData().observe(viewLifecycleOwner) {
                showToast(binding.tietPlaylistTitle.text.toString())
            }
            Log.d(TAG, "create playlist button2")
            showToast(title = binding.tietPlaylistTitle.text.toString())
            findNavController().navigateUp()
        }
    }

    fun showToast(title: String) {
        Log.d(TAG, "GET SHOW TOAST")

        Toast.makeText(
            requireContext(), getString(R.string.playlist_created, title), Toast.LENGTH_LONG
        ).show()
    }
}