package ru.huza.core.model.dto

data class UserSaveModel(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val role: String? = null,
    val avatarUrl: String? = null,
)
