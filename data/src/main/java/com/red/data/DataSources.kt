package com.red.data

import com.red.domain.User

interface AuthDataSource {
    fun getCurrentUser(): User?
    fun signOut()
}
