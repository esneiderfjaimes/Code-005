package com.red.usecases

import android.content.Context
import com.red.data.AuthRepository
import com.red.domain.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(): User? = repository.getCurrentUser()
}

class IsLoginUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(): Boolean = repository.getCurrentUser() != null
}

class AuthGoogleIntentUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(context: Context) = repository.authWithGoogleIntent(context)
}

class AuthGoogleUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(idToken: String) = repository.authWithGoogle(idToken)
}

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke() {
        repository.signOut()
    }
}
