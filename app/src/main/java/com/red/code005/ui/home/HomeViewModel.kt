package com.red.code005.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.red.code005.ui.common.Event
import com.red.usecases.GetUserUseCase
import com.red.usecases.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    isLoginUseCase: IsLoginUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    // region Fields

    private val _events = MutableLiveData<Event<HomeNavigation>>()
    val events: LiveData<Event<HomeNavigation>> get() = _events

    // endregion

    // region Override Methods & Callbacks

    init {
       if (!isLoginUseCase.invoke()) _events.value = Event(HomeNavigation.GoSignIn)
    }

    // endregion

    // region Public Methods

    fun onUserPhoto(context: Context, onResourceReady: (Bitmap) -> Unit) {
        Glide.with(context).asBitmap()
            .load(getUserUseCase.invoke()?.photoUrl.toString())
            .circleCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    onResourceReady.invoke(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // no use
                }
            })
    }

    // endregion

    // region Companion Object

    companion object {
        const val tag = "HomeFragment"
    }

    // endregion

    // region Inner Classes & Interfaces

    sealed class HomeNavigation {
        data class ShowError(val error: Throwable) : HomeNavigation()
        object GoSignIn : HomeNavigation()
    }

    // endregion

}