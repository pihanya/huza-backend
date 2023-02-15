package ru.huza.core.model.dto

data class UserPatchModel(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val role: String? = null,
    val avatarUrl: String? = null,
)
