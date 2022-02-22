package com.red.data

import android.content.Context
import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.red.domain.ProfilePreview
import com.red.domain.RootProfile
import com.red.domain.User
import io.reactivex.Maybe

interface AuthDataSource {
    fun getCurrentUser(): User?
    fun authWithGoogleIntent(context: Context): Intent
    fun authWithGoogle(idToken: String): Task<AuthResult>
    fun signOut()
}

interface ProfileDataSource {
    fun profileByID(id: String): Maybe<RootProfile>
    fun profileByUsername(username: String): Maybe<ProfilePreview>
}