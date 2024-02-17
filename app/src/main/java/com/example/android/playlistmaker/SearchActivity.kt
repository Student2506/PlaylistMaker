package com.example.android.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.datalayer.Track
import com.example.android.playlistmaker.mocks.trackList
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val headerButton = findViewById<FrameLayout>(R.id.back_to_main)
        headerButton.setOnClickListener {
            finish()
        }
        inputEditText.setText(searchQuery)


        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = getClearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchQuery = inputEditText.text.toString()
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        val recycler = findViewById<RecyclerView>(R.id.trackList)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TrackAdapter(
            tracks = List(100) {
                val randomTrack = trackList[Random.nextInt(0, trackList.size-1)]
                Track(
                    trackName = randomTrack["trackName"]!!,
                    artistName = randomTrack["artist"]!!,
                    trackTime = randomTrack["duration"]!!,
                    artworkUrl100 = randomTrack["link"]!!
                )
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY, DEFAULT_QUERY)
    }

    private fun getClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val DEFAULT_QUERY = ""
    }
}