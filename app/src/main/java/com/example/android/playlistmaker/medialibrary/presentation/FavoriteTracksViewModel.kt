package com.example.android.playlistmaker.medialibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoritesInteractor: FavoritesInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun onStart() {
        stateLiveData.postValue(FavoriteState.Loading)
        viewModelScope.launch {
            favoritesInteractor.favoriteTracks().collect { tracks ->
                if (tracks.isEmpty()) {
                    stateLiveData.postValue(FavoriteState.Empty)
                } else {
                    stateLiveData.postValue(FavoriteState.Content(tracks))
                }
            }
        }
    }
}