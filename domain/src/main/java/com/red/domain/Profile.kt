package com.red.domain

const val collectionProfile = "profiles"

const val fieldName = "name"
const val fieldUserName = "username"
const val fieldState = "state"
const val fieldPhotoUrl = "photo URL"

const val fieldConnections = "connections"

val profilePreviewRequiredFields = listOf(fieldName, fieldUserName)
val rootProfileRequiredFields = listOf(fieldName, fieldUserName)

data class ProfilePreview(
    val id: String,
    val name: String,
    val username: String,
    val state: String?,
    val photoUrl: String?
)

data class RootProfile(
    val id: String,
    val name: String,
    val username: String,
    val state: String?,
    val photoUrl: String?,
    val connections: List<String> = listOf()
)
