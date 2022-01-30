package com.red.code005.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.red.code005.ui.common.Event
import com.red.code005.ui.account.AccountViewModel.AccountNavigation.SignOut
import com.red.usecases.GetUserUseCase
import com.red.usecases.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    // region Fields

    private val _events = MutableLiveData<Event<AccountNavigation>>()
    val events: LiveData<Event<AccountNavigation>> get() = _events

    val user = getUserUseCase.invoke()

    // endregion

    // region Public Methods

    fun signOut() {
        signOutUseCase.invoke()
        _events.value = Event(SignOut)
    }

    // endregion

    // region Inner Classes & Interfaces

    sealed class AccountNavigation {
        data class ShowError(val error: Throwable) : AccountNavigation()
        object SignOut : AccountNavigation()
    }

    // endregion

}