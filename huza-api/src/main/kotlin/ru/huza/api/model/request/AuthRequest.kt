package ru.huza.api.model.request

data class AuthRequest(
    val username: String?,
    val email: String?,
    val password: String,
)
