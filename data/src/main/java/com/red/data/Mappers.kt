package com.red.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.red.domain.*

fun FirebaseUser.toUser() = User(
    uid = uid,
    name = displayName,
    email = email,
    photoUrl = photoUrl?.toString()
)

fun MutableMap<String, Any>?.toProfilePreview(id: String) = if (isNullOrEmpty()) null else {
    profilePreviewRequiredFields.forEach { if (get(it) == null) return null }
    ProfilePreview(
        id = id,
        name = get(fieldName) as String,
        username = get(fieldUserName) as String,
        state = get(fieldState) as String?,
        photoUrl = get(fieldPhotoUrl) as String?
    )
}

fun MutableMap<String, Any>?.toRootProfile(id: String) = if (isNullOrEmpty()) null else {
    rootProfileRequiredFields.forEach { if (get(it) == null) return null }
    val references = get(fieldConnections) as ArrayList<*>?
    val connections = mutableListOf<String>()
    references?.forEach { if (it is DocumentReference) connections.add(it.id) }
    RootProfile(
        id = id,
        name = get(fieldName) as String,
        username = get(fieldUserName) as String,
        state = get(fieldState) as String?,
        photoUrl = get(fieldPhotoUrl) as String?,
        connections = connections.toList()
    )
}