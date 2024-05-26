package com.example.android.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.main.CustomApp
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private val sharedPreferencesInteractor =
        Creator.provideSharedPreferncesInteractor(this.application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val header = findViewById<FrameLayout>(R.id.flBackToMainSettings)
        header.setOnClickListener {
            finish()
        }

        val licenseAgreement = findViewById<FrameLayout>(R.id.flShowAgreement)
        licenseAgreement.setOnClickListener {
            val showAgreement = Intent(Intent.ACTION_VIEW)
            showAgreement.data = Uri.parse(getString(R.string.license_agreement_url))
            startActivity(showAgreement)
        }

        val techSupport = findViewById<FrameLayout>(R.id.flSendToTechsupport)
        techSupport.setOnClickListener {
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

        val shareApp = findViewById<FrameLayout>(R.id.flShareButton)
        shareApp.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
            startActivity(shareAppIntent)
        }


        val themeSwitcher = findViewById<SwitchMaterial>(R.id.swNightMode)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as CustomApp).switchTheme(checked)
            sharedPreferencesInteractor.putDarkThemePref(
                checked,
                object : SharedPreferencesInteractor.SharedPreferencesConsumer {
                    override fun consume(result: Any) {
                        Log.d(TAG, result.toString())
                    }
                })
        }
        themeSwitcher.isChecked = (applicationContext as CustomApp).darkTheme
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val KEY_FOR_NIGHT_THEME = "key_for_night_theme"
        const val KEY_FOR_SEARCH_HISTORY = "key_for_search_activity"
        private const val TAG = "SettingsActivity"
    }
}