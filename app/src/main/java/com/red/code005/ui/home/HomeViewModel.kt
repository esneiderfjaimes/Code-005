package com.red.code005.ui.home

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.red.code005.utils.loadBitmapFromUrl
import com.red.usecases.CurrentUserUseCase
import com.red.usecases.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    isLogin: IsLoginUseCase,
    private val currentUser: CurrentUserUseCase,
) : ViewModel() {

    // region Fields

    private val _event = MutableLiveData<HomeEvent>()
    val event: LiveData<HomeEvent> get() = _event

    // endregion

    // region Override Methods & Callbacks

    init {
        if (!isLogin.invoke()) _event.postValue(HomeEvent.GoSignIn)
    }

    // endregion

    // region Public Methods

    fun onUserPhoto(context: Context, onResourceReady: (Bitmap) -> Unit) {
        loadBitmapFromUrl(context, currentUser.invoke()?.photoUrl.toString(), onResourceReady)
    }

    // endregion

    // region Inner Classes & Interfaces

    sealed class HomeEvent {
        data class ShowError(val error: Throwable) : HomeEvent()
        object GoSignIn : HomeEvent()
    }

    // endregion

}