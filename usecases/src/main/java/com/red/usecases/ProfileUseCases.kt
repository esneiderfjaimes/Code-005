package com.red.usecases

import com.red.data.ProfileRepository
import javax.inject.Inject

class CurrentProfileUserCase @Inject constructor(private val repository: ProfileRepository) {
    fun invoke(uidAuth: String) = repository.profileByID(uidAuth)
}

class ProfileByUsernameUseCase @Inject constructor(private val repository: ProfileRepository) {
    fun invoke(username: String) = repository.profileByUsername(username)
}

