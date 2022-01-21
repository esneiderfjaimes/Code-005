package com.red.code005.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.red.code005.R

class LoginRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    fun authWithGoogleIntent(context: Context) = GoogleSignIn.getClient(
        context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
    ).signInIntent

    fun authWithGoogle(idToken: String) = auth.signInWithCredential(
        GoogleAuthProvider.getCredential(idToken, null)
    )
}