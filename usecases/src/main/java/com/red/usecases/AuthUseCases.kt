package com.red.usecases

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import com.red.data.AuthRepository
import com.red.domain.User
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(): User? = repository.currentUser()
}

class IsLoginUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(): Boolean = repository.currentUser() != null
}

class AuthGoogleIntentUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(context: Context) = repository.authWithGoogleIntent(context)
}

class AuthGoogleUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(activity: Activity, result: ActivityResult?) = repository.authWithGoogle(activity, result)
}

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke() {
        repository.signOut()
    }
}
