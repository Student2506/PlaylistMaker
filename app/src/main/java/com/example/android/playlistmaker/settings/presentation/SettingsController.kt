package com.example.android.playlistmaker.settings.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.FrameLayout
import com.example.android.playlistmaker.R
import com.example.android.playlistmaker.creator.Creator
import com.example.android.playlistmaker.main.CustomApp
import com.example.android.playlistmaker.search.domain.api.SharedPreferencesInteractor
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsController(private val activity: Activity) {

    private var sharedPreferencesInteractor: SharedPreferencesInteractor? = null
    private var header: FrameLayout? = null
    private var licenseAgreement: FrameLayout? = null
    private var techSupport: FrameLayout? = null
    private var shareApp: FrameLayout? = null
    private var themeSwitcher: SwitchMaterial? = null

    fun onCreate() {
        sharedPreferencesInteractor = Creator.provideSharedPreferncesInteractor(activity)
        header = activity.findViewById<FrameLayout>(R.id.flBackToMainSettings)
        header?.setOnClickListener {
            activity.finish()
        }

        licenseAgreement = activity.findViewById<FrameLayout>(R.id.flShowAgreement)
        licenseAgreement?.setOnClickListener {
            val showAgreement = Intent(Intent.ACTION_VIEW)
            showAgreement.data = Uri.parse(activity.getString(R.string.license_agreement_url))
            activity.startActivity(showAgreement)
        }
        techSupport = activity.findViewById<FrameLayout>(R.id.flSendToTechsupport)
        techSupport?.setOnClickListener {
            val techSupportIntent = Intent(Intent.ACTION_SENDTO)
            techSupportIntent.data = Uri.parse("mailto:")
            techSupportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(activity.getString(R.string.main_email_address))
            )
            techSupportIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                activity.getString(R.string.generic_email_title_template)
            )
            techSupportIntent.putExtra(
                Intent.EXTRA_TEXT,
                activity.getString(R.string.generic_email_body_template)
            )
            activity.startActivity(techSupportIntent)
        }
        shareApp = activity.findViewById<FrameLayout>(R.id.flShareButton)
        shareApp?.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.practicum_link))
            activity.startActivity(shareAppIntent)
        }

        themeSwitcher = activity.findViewById<SwitchMaterial>(R.id.swNightMode)
        themeSwitcher?.setOnCheckedChangeListener { _, checked ->
            (activity.applicationContext as CustomApp).switchTheme(checked)
            sharedPreferencesInteractor?.putDarkThemePref(
                checked,
                object : SharedPreferencesInteractor.SharedPreferencesConsumer {
                    override fun consume(result: Any) {
                        Log.d(TAG, result.toString())
                    }
                })
        }
        themeSwitcher?.isChecked = (activity.applicationContext as CustomApp).darkTheme
    }

    companion object {
        private const val TAG = "SettingsController"
    }
}