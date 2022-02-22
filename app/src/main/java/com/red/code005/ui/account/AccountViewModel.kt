package com.red.code005.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.red.code005.ui.account.AccountViewModel.AccountEvent.SignOut
import com.red.usecases.CurrentUserUseCase
import com.red.usecases.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    currentUser: CurrentUserUseCase,
    private val signOut: SignOutUseCase
) : ViewModel() {

    // region Fields

    private val _event = MutableLiveData<AccountEvent>()
    val event: LiveData<AccountEvent> get() = _event

    val user = currentUser.invoke()

    // endregion

    // region Public Methods

    fun signOut() {
        signOut.invoke()
        _event.postValue(SignOut)
    }

    // endregion

    // region Inner Classes & Interfaces

    sealed class AccountEvent {
        data class ShowError(val error: Throwable) : AccountEvent()
        object SignOut : AccountEvent()
    }

    // endregion

}