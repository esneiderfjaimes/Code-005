package com.red.usecases

import com.red.data.AuthRepository
import com.red.domain.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(): User? = repository.getCurrentUser()
}

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke() {
        repository.signOut()
    }
}
