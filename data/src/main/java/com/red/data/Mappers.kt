package com.red.data

import com.google.firebase.auth.FirebaseUser
import com.red.domain.User

fun FirebaseUser.toDomain() = User(
    displayName,
    email,
    if (photoUrl != null) photoUrl.toString() else null
)
