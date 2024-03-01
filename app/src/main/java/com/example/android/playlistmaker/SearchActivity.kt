package com.example.android.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.playlistmaker.api.v1.ITunesApi
import com.example.android.playlistmaker.datalayer.ITunesTrackResponse
import com.example.android.playlistmaker.datalayer.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection.HTTP_OK

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = ""
    private val baseUrl: String = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()

    private lateinit var recycler: RecyclerView
    private lateinit var nothingFound: TextView
    private lateinit var refreshButton: Button
    private lateinit var noConnection: TextView

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
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    searchSong(inputEditText.text.toString())
                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                true
            } else {
                false
            }
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            tracks.clear()
            adapter.notifyDataSetChanged()
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

        recycler = findViewById(R.id.trackList)
        nothingFound = findViewById(R.id.nothing_found)
        refreshButton = findViewById(R.id.refresh_button)
        noConnection = findViewById(R.id.no_signal)
        adapter.tracks = tracks
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

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

    private fun searchSong(songTitle: String) {
        itunesService.getTracks(songTitle).enqueue(object : Callback<ITunesTrackResponse> {
            override fun onResponse(
                call: Call<ITunesTrackResponse>, response: Response<ITunesTrackResponse>
            ) {
                when (response.code()) {
                    HTTP_OK -> {
                        refreshButton.visibility = View.GONE
                        noConnection.visibility = View.GONE
                        if (response.body()?.tracks?.isNotEmpty() == true) {
                            nothingFound.visibility = View.GONE
                            tracks.clear()
                            tracks.addAll(response.body()?.tracks!!)
                            recycler.visibility = View.VISIBLE
                            adapter.notifyDataSetChanged()
                        } else {
                            tracks.clear()
                            adapter.notifyDataSetChanged()
                            recycler.visibility = View.GONE
                            nothingFound.visibility = View.VISIBLE
                        }
                    }

                    else -> {
                        showErrorMessage()
                    }
                }
            }

            override fun onFailure(call: Call<ITunesTrackResponse>, t: Throwable) {
                showErrorMessage()
            }
        })
    }

    private fun showErrorMessage() {
        tracks.clear()
        adapter.notifyDataSetChanged()
        recycler.visibility = View.GONE
        nothingFound.visibility = View.GONE
        noConnection.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE
    }

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val DEFAULT_QUERY = ""
    }
}