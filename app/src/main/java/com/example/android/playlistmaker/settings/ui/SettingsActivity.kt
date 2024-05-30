package com.example.android.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.main.CustomApp
import com.example.android.playlistmaker.settings.presentation.SettingsView
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity(), SettingsView {

    private val settingsPresenter = Creator.provideSettingsPresenter(this, this)

    private var header: FrameLayout? = null
    private var licenseAgreement: FrameLayout? = null
    private var techSupport: FrameLayout? = null
    private var shareApp: FrameLayout? = null
    private var themeSwitcher: SwitchMaterial? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        header = findViewById<FrameLayout>(R.id.flBackToMainSettings)
        licenseAgreement = findViewById<FrameLayout>(R.id.flShowAgreement)
        techSupport = findViewById<FrameLayout>(R.id.flSendToTechsupport)
        shareApp = findViewById<FrameLayout>(R.id.flShareButton)
        themeSwitcher = findViewById<SwitchMaterial>(R.id.swNightMode)
        settingsPresenter.onCreate()
        header?.setOnClickListener {
            finish()
        }


        licenseAgreement?.setOnClickListener {
            val showAgreement = Intent(Intent.ACTION_VIEW)
            showAgreement.data = Uri.parse(getString(R.string.license_agreement_url))
            startActivity(showAgreement)
        }

        techSupport?.setOnClickListener {
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
        shareApp?.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
            startActivity(shareAppIntent)
        }


        themeSwitcher?.setOnCheckedChangeListener { _, checked ->
            (applicationContext as CustomApp).switchTheme(checked)
            settingsPresenter.themeSwitcher(checked)
        }
        themeSwitcher?.isChecked = (applicationContext as CustomApp).darkTheme
        settingsPresenter.onCreate()
    }

    companion object {
        private const val TAG = "SettingsActivity"
    }
}
