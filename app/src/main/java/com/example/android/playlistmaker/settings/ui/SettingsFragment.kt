package com.example.android.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.playlistmaker.PlaylistMakerApp
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.FragmentSettingsBinding
import com.example.android.playlistmaker.settings.presentation.SettingsViewModel
import com.example.android.playlistmaker.util.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.flShowAgreement.setOnClickListener {
            val showAgreement = Intent(Intent.ACTION_VIEW)
            showAgreement.data = Uri.parse(getString(R.string.license_agreement_url))
            startActivity(showAgreement)
        }
        binding.flSendToTechsupport.setOnClickListener {
            val techSupportIntent = Intent(Intent.ACTION_SENDTO)
            techSupportIntent.data = Uri.parse("mailto:")
            techSupportIntent.putExtra(
                Intent.EXTRA_EMAIL, arrayOf(getString(R.string.main_email_address))
            )
            techSupportIntent.putExtra(
                Intent.EXTRA_SUBJECT, getString(R.string.generic_email_title_template)
            )
            techSupportIntent.putExtra(
                Intent.EXTRA_TEXT, getString(R.string.generic_email_body_template)
            )
            startActivity(techSupportIntent)
        }
        binding.flShareButton.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
            startActivity(shareAppIntent)
        }
        binding.swNightMode.setOnCheckedChangeListener { _, checked ->
            (requireContext().applicationContext as PlaylistMakerApp).switchTheme(checked)
            settingsViewModel.themeSwitcher(checked)
        }
        binding.swNightMode.isChecked =
            (requireContext().applicationContext as PlaylistMakerApp).darkTheme
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
        private const val TAG = "SettingsActivity"
    }
}