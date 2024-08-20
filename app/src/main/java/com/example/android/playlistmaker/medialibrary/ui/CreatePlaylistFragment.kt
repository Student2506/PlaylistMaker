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
import androidx.activity.OnBackPressedCallback
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
import com.example.android.playlistmaker.util.extensions.showCustomToast
import com.example.android.playlistmaker.util.ui.BindingFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = CreatePlaylistFragment()

        const val TAG = "CreatePlaylistFragment"
        private const val ROUND_CORNERS_SIZE_PX = 8f
    }

    private val viewModel by viewModel<CreatePlaylistViewModel>()

    private var isTitleEmpty = true
    private var isDescriptionEmpty = true
    private var isCoverEmpty = true
    private var confirmDialog: MaterialAlertDialogBuilder? = null

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
        confirmDialog =
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.is_finishing_playlist_creation))
                .setPositiveButton(getString(R.string.finish_option)) { _, _ ->
                    findNavController().navigateUp()
                }.setNegativeButton(getString(R.string.cancel_option)) { _, _ ->

                }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!isCoverEmpty || !isTitleEmpty || !isDescriptionEmpty) {
                        confirmDialog?.show()
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })
        binding.tbToolbar.setOnClickListener {
            if (!isCoverEmpty || !isTitleEmpty || !isDescriptionEmpty) {
                confirmDialog?.show()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.tietPlaylistTitle.addTextChangedListener(beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                binding.bCreatePlaylist.isEnabled = !charSequence.isNullOrEmpty()
                isTitleEmpty = charSequence.isNullOrEmpty()
            },
            afterTextChanged = { _ -> })

        binding.tietPlaylistDescription.addTextChangedListener(beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                isDescriptionEmpty = charSequence.isNullOrEmpty()
            },
            afterTextChanged = { _ -> })

        viewModel.observeState().observe(viewLifecycleOwner) { uri ->
            binding.ivPlaylistCover.setImageURI(uri)
        }
        viewModel.observeToastLiveData().observe(viewLifecycleOwner) {
            Toast(requireContext()).showCustomToast(
                getString(
                    R.string.playlist_created, binding.tietPlaylistTitle.text.toString()
                ), requireActivity()
            )
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
            isCoverEmpty = binding.ivPlaylistCover.paddingTop == 0
        }
        binding.bCreatePlaylist.setOnClickListener {
            Log.d(TAG, "create playlist button")

            viewModel.savePlaylist(
                binding.tietPlaylistTitle.text.toString(),
                binding.tietPlaylistDescription.text.toString()
            )

            findNavController().navigateUp()
        }
    }
}