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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.red.code005.R
import com.red.code005.ui.common.Event
import com.red.usecases.AuthGoogleIntentUseCase
import com.red.usecases.AuthGoogleUseCase
import com.red.usecases.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    getUserUseCase: IsLoginUseCase,
    private val authGoogleIntentUseCase: AuthGoogleIntentUseCase,
    private val authGoogleUseCase: AuthGoogleUseCase
) : ViewModel() {

    // region Fields

    lateinit var result: ActivityResultLauncher<Intent>
    private val _events = MutableLiveData<Event<LoginNavigation>>()
    val events: LiveData<Event<LoginNavigation>> get() = _events

    // endregion

    // region Override Methods & Callbacks

    init {
        if (getUserUseCase.invoke()) _events.value = Event(LoginNavigation.SignIn)
    }

    // endregion

    // region Public Methods

    fun launchSignInGoogle(activity: Fragment) {
        result.launch(authGoogleIntentUseCase.invoke(activity.requireContext()))
    }

    fun authWithGoogle(activity: Activity, result: ActivityResult?) {
        if (result != null) try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d(tag, "firebaseAuthWithGoogle:" + account.id)
            authGoogleUseCase.invoke(account.idToken!!).addOnCompleteListener(activity) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = it.result.user
                    Log.d(tag, "signInWithCredential:success UID user:${user?.uid}")
                    _events.value = Event(LoginNavigation.SignIn)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "signInWithCredential:failure", it.exception)
                    _events.value = Event(LoginNavigation.ShowToastError(R.string.login_failed))
                }
            }.addOnFailureListener {
                Log.w(tag, "signInWithCredential:failure", it)
                _events.value = Event(LoginNavigation.ShowToastError(R.string.login_failed))
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(tag, "Google sign in failed", e)
            _events.value = Event(LoginNavigation.ShowToastError(R.string.login_failed))
        }
    }

    // endregion

    // region Companion Object

    companion object {
        const val tag = "LoginFragment"
    }

    // endregion

    // region Inner Classes & Interfaces

    sealed class LoginNavigation {
        data class ShowError(val error: Throwable) : LoginNavigation()
        data class ShowToastError(@StringRes val resId: Int) : LoginNavigation()
        object SignIn : LoginNavigation()
    }

    // endregion

}