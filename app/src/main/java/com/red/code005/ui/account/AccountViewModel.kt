package com.red.code005.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.red.code005.ui.account.AccountViewModel.AccountEvent.SignOut
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

    private val _event = MutableLiveData<AccountEvent>()
    val event: LiveData<AccountEvent> get() = _event

    val user = getUserUseCase.invoke()

    // endregion

    // region Public Methods

    fun signOut() {
        signOutUseCase.invoke()
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