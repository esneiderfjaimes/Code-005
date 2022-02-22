package com.red.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.red.domain.ProfilePreview
import com.red.domain.RootProfile
import com.red.domain.User
import io.reactivex.Maybe

interface AuthDataSource {
    fun currentUser(): User?
    fun authWithGoogleIntent(context: Context): Intent
    fun authWithGoogle(activity: Activity, activityResult: ActivityResult?): Maybe<User>
    fun signOut()
}

interface ProfileDataSource {
    fun profileByID(id: String): Maybe<RootProfile>
    fun profileByUsername(username: String): Maybe<ProfilePreview>
}