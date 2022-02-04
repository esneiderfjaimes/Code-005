package com.red.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.red.domain.User
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthDataSource {

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomain()
    }

    override fun authWithGoogleIntent(context: Context) = GoogleSignIn.getClient(
        context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
    ).signInIntent

    override fun authWithGoogle(idToken: String) = firebaseAuth.signInWithCredential(
        GoogleAuthProvider.getCredential(idToken, null)
    )

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
