package com.red.data

import com.google.firebase.auth.FirebaseAuth
import com.red.domain.User
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthDataSource {

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomain()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
