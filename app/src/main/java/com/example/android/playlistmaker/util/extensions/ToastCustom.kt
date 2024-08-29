package com.example.android.playlistmaker.util.extensions

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.android.playlistmaker.R

fun Toast.showCustomToast(message: String, activity: Activity) {
    val layout = activity.layoutInflater.inflate(
        R.layout.custom_toast,
        activity.findViewById(R.id.toast_container)
    )

    val textView = layout.findViewById<TextView>(R.id.message)
    textView.text = message

    this.apply {
        setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 16)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}