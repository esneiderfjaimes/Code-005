package com.red.code005.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.red.code005.ui.profile.ProfileViewModel.Action.*
import com.red.code005.ui.profile.ProfileViewModel.ProfileEvent.ActionError
import com.red.code005.ui.profile.ProfileViewModel.ProfileEvent.ShowAction
import com.red.domain.ProfilePreview
import com.red.usecases.CurrentProfileUserCase
import com.red.usecases.GetUserUseCase
import com.red.usecases.ProfileByUsernameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentUser: GetUserUseCase,
    private val currentProfile: CurrentProfileUserCase,
    private val profileByUsernameUseCase: ProfileByUsernameUseCase,
) : ViewModel() {

    // region Fields

    private val disposable = CompositeDisposable()

    private val _event = MutableLiveData<ProfileEvent>()
    val event: LiveData<ProfileEvent> get() = _event

    // endregion

    // region Override Methods & Callbacks

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    // endregion

    // region Public Methods

    fun consultProfile(username: String) {
        profileByUsernameUseCase.invoke(username).subscribe(
            { profile ->
                _event.postValue(ProfileEvent.ShowProfile(profile))
            }, { exception ->
                _event.postValue(ProfileEvent.ProfileError(exception))
            }, {
                _event.postValue(ProfileEvent.ProfileNotFound)
            }
        ).let { disposable.add(it) }
    }

    fun genAction(id: String) {
        currentUser.invoke()?.uid?.let { uid ->
            currentProfile.invoke(uid)
                .subscribe({ rootProfile ->
                    _event.postValue(
                        ShowAction(
                            when {
                                rootProfile.id == id -> SHARE
                                rootProfile.connections.indexOf(id) != -1 -> CONNECTED
                                else -> CONNECT
                            }
                        )
                    )
                }, { exception ->
                    _event.postValue(ActionError(exception))
                }, {
                    _event.postValue(ShowAction(NO_ACTION))
                }
                ).let { disposable.add(it) }
        } ?: _event.postValue(ShowAction(NO_ACTION))
    }

    // endregion

    // region Inner Classes & Interfaces

    sealed class ProfileEvent {
        data class ShowProfile(val profilePreview: ProfilePreview) : ProfileEvent()
        data class ProfileError(val exception: Throwable) : ProfileEvent()
        object ProfileNotFound : ProfileEvent()
        data class ShowAction(val action: Action) : ProfileEvent()
        data class ActionError(val exception: Throwable) : ProfileEvent()
    }

    enum class Action {
        NO_ACTION,
        CONNECT,
        CONNECTED,
        SHARE
    }

    // endregion

}