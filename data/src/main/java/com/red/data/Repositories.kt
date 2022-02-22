package com.red.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.red.domain.*
import io.reactivex.Maybe
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthDataSource {

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
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

class ProfileRepository @Inject constructor(private val firestore: FirebaseFirestore) :
    ProfileDataSource {

    override fun profileByID(id: String): Maybe<RootProfile> =
        Maybe.create { emitter ->
            firestore.collection(collectionProfile)
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    val profile = if (!document.exists()) null
                    else document.data.toRootProfile(document.id)
                    profile?.let { emitter.onSuccess(it) } ?: emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }

    override fun profileByUsername(username: String): Maybe<ProfilePreview> =
        Maybe.create { emitter ->
            firestore.collection(collectionProfile)
                .whereEqualTo(fieldUserName, username)
                .limit(1)
                .get()
                .addOnSuccessListener { query ->
                    val documents = query.documents
                    val profile = if (documents.isNullOrEmpty() || !documents[0].exists()) null
                    else documents[0].data.toProfilePreview(documents[0].id)
                    profile?.let { emitter.onSuccess(it) } ?: emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
}
