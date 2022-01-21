package com.red.code005.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.red.code005.R
import com.red.code005.data.LoginRepository

class LoginViewModel(
    private val result: ActivityResultLauncher<Intent>,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    // Sign in with Google

    fun launchSignInGoogle(activity: Fragment) {
        result.launch(loginRepository.authWithGoogleIntent(activity.requireContext()))
    }

    fun authWithGoogle(activity: Activity, result: ActivityResult?) {
        if (result != null) try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d(tag, "firebaseAuthWithGoogle:" + account.id)
            loginRepository.authWithGoogle(account.idToken!!)
                .addOnCompleteListener(activity) {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = it.result.user
                        Log.d(tag, "signInWithCredential:success UID user:${user?.uid}")
                        _loginResult.value =
                            LoginResult(
                                success = LoggedInUserView(
                                    displayName = user?.displayName ?: "N/A"
                                )
                            )
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(tag, "signInWithCredential:failure", it.exception)
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                }.addOnFailureListener {
                    Log.w(tag, "signInWithCredential:failure", it)
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(tag, "Google sign in failed", e)
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    companion object {
        const val tag = "LoginFragment"
    }
}