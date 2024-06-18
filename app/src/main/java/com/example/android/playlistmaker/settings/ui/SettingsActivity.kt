package com.example.android.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.playlistmaker.PlaylistMakerApp
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.databinding.ActivitySettingsBinding
import com.example.android.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tbToolbar.setOnClickListener {
            finish()
        }
        binding.flShowAgreement.setOnClickListener {
            val showAgreement = Intent(Intent.ACTION_VIEW)
            showAgreement.data = Uri.parse(getString(R.string.license_agreement_url))
            startActivity(showAgreement)
        }
        binding.flSendToTechsupport.setOnClickListener {
            val techSupportIntent = Intent(Intent.ACTION_SENDTO)
            techSupportIntent.data = Uri.parse("mailto:")
            techSupportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.main_email_address))
            )
            techSupportIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.generic_email_title_template)
            )
            techSupportIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.generic_email_body_template)
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
            (applicationContext as PlaylistMakerApp).switchTheme(checked)
            settingsViewModel.themeSwitcher(checked)
        }
        binding.swNightMode.isChecked = (applicationContext as PlaylistMakerApp).darkTheme

    }

    companion object {
        private const val TAG = "SettingsActivity"
    }
}
