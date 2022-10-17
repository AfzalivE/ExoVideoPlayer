package com.afzaln.exovideoplayer

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoViewModel: ViewModel() {

    private val _videoUri: MutableLiveData<Uri> = MutableLiveData()
    val videoUri: LiveData<Uri> get() = _videoUri

    fun updateVideo(uri: Uri) {
        _videoUri.value = uri
    }
}
