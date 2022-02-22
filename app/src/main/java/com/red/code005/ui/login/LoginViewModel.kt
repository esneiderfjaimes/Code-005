package com.red.code005.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.red.code005.R
import com.red.code005.ui.login.LoginViewModel.LoginEvent.ShowToastError
import com.red.code005.ui.login.LoginViewModel.LoginEvent.SignIn
import com.red.usecases.AuthGoogleIntentUseCase
import com.red.usecases.AuthGoogleUseCase
import com.red.usecases.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val isLogin: IsLoginUseCase,
    private val authGoogleIntent: AuthGoogleIntentUseCase,
    private val authGoogle: AuthGoogleUseCase,
) : ViewModel() {

    // region Fields
    private val disposable = CompositeDisposable()

    lateinit var result: ActivityResultLauncher<Intent>
    private val _event = MutableLiveData<LoginEvent>()
    val event: LiveData<LoginEvent> get() = _event

    // endregion

    // region Override Methods & Callbacks

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    // endregion

    // region Public Methods

    fun launchSignInGoogle(activity: Fragment) {
        result.launch(authGoogleIntent.invoke(activity.requireContext()))
    }

    fun authWithGoogle(activity: Activity, result: ActivityResult?) {
        authGoogle.invoke(activity, result).subscribe(
            { user ->
                Log.d(tag, "signInWithCredential:success UID user:${user.uid}")
                _event.postValue(SignIn)
            }, { exception ->
                Log.e(tag, "signInWithCredential:failure", exception)
                _event.postValue(ShowToastError(R.string.login_failed))
            }, {
                _event.postValue(ShowToastError(R.string.login_failed))
            }
        ).let { disposable.add(it) }
    }

    // endregion

    // region Companion Object

    companion object {
        const val tag = "LoginFragment"
    }

    // endregion

    // region Inner Classes & Interfaces

    sealed class LoginEvent {
        data class ShowError(val error: Throwable) : LoginEvent()
        data class ShowToastError(@StringRes val resId: Int) : LoginEvent()
        object SignIn : LoginEvent()
    }

    // endregion

}