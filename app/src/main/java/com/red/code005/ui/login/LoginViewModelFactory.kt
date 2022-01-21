package com.red.code005.ui.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.red.code005.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(private val result: ActivityResultLauncher<Intent>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                result = result,
                loginRepository = LoginRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}