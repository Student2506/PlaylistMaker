package com.example.android.playlistmaker.medialibrary.presentation

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.playlistmaker.PlaylistMakerApp
import com.example.android.playlistmaker.medialibrary.domain.api.PlaylistInteractor
import com.example.android.playlistmaker.medialibrary.domain.models.Playlist
import com.example.android.playlistmaker.medialibrary.domain.models.PlaylistTrack
import com.example.android.playlistmaker.util.SingleLiveEvent
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.io.path.createTempFile

open class CreatePlaylistViewModel(
    application: Application,
    private val playlistInteractor: PlaylistInteractor,
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "CreatePlaylistViewModel"
    }

    protected val stateLiveData = MutableLiveData<Uri>()
    fun observeState(): LiveData<Uri> = stateLiveData
    protected val toastLiveData = SingleLiveEvent<String>()
    fun observeToastLiveData(): LiveData<String> = toastLiveData

    private var currentUri: Uri? = null

    fun chooseFileCopyToCache(uri: Uri): Uri? {
        val filePath = createTempFile()
        val file = File(filePath.toUri())
        val inputStream = getApplication<PlaylistMakerApp>().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        inputStream?.close()
        stateLiveData.postValue(file.toUri())
        currentUri = file.toUri()
        return currentUri
    }

    fun saveImageToPrivateStorage(albumTitle: String, image: String?): Uri? {
        val filePath = File(
            getApplication<PlaylistMakerApp>().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "myalbums"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        if (image != null) {
            val inputStream =
                getApplication<PlaylistMakerApp>().contentResolver.openInputStream(image.toUri())
            val file = File(filePath, albumTitle)

            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream?.read(buffer).also { read = it!! } != -1) {
                outputStream.write(buffer, 0, read)
            }
            inputStream?.close()
            outputStream.flush()
            outputStream.close()
            stateLiveData.postValue(file.toUri())
            currentUri = file.toUri()
            return currentUri
        }
        return null
    }

    open fun savePlaylist(title: String, description: String?) {
        saveImageToPrivateStorage(title, currentUri?.toString())
        viewModelScope.launch {
            playlistInteractor.createPlaylist(
                Playlist(
                    id = 0L,
                    title = title,
                    description = description,
                    tracks = emptyList<PlaylistTrack>(),
                    imageUrl = currentUri?.toString()
                )
            )
        }
        toastLiveData.postValue(title)
    }
}