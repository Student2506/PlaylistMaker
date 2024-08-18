package com.example.android.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.android.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.android.playlistmaker.medialibrary.presentation.CreatePlaylistViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = CreatePlaylistFragment()

        const val TAG = "CreatePlaylistFragment"
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
                    binding.ivPlaylistCover.setImageURI(uri)
                    viewModel.chooseFileCopyToCache(uri)
                } else {
                    Log.d(TAG, "No media selected")
                }
            }
        binding.ivPlaylistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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