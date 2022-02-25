package com.red.code005.ui.home

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.red.code005.utils.loadBitmapFromUrl
import com.red.usecases.CurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currentUser: CurrentUserUseCase,
) : ViewModel() {

    // region Public Methods

    fun onUserPhoto(context: Context, onResourceReady: (Bitmap) -> Unit) {
        loadBitmapFromUrl(context, currentUser.invoke()?.photoUrl.toString(), onResourceReady)
    }

    // endregion

}