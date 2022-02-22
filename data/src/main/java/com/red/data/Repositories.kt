package com.red.data

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.red.domain.*
import io.reactivex.Maybe
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthDataSource {

    override fun currentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
    }

    override fun authWithGoogleIntent(context: Context) = GoogleSignIn.getClient(
        context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
    ).signInIntent

    override fun authWithGoogle(activity: Activity, activityResult: ActivityResult?) =
        Maybe.create<User> { emitter ->
            try {
                val idToken = GoogleSignIn.getSignedInAccountFromIntent(activityResult!!.data)
                    .getResult(ApiException::class.java)!!.idToken
                firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            task.result.user?.let { firebaseUser -> emitter.onSuccess(firebaseUser.toUser()) }
                                ?: emitter.onComplete()
                        } else {
                            emitter.onComplete()
                        }
                    }.addOnFailureListener { exception -> emitter.onError(exception) }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }


    override fun signOut() {
        firebaseAuth.signOut()
    }
}

class ProfileRepository @Inject constructor(private val firestore: FirebaseFirestore) :
    ProfileDataSource {

    override fun profileByID(id: String) = Maybe.create<RootProfile> { emitter ->
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

    override fun profileByUsername(username: String) = Maybe.create<ProfilePreview> { emitter ->
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
