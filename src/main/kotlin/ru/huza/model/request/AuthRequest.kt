package ru.huza.model.request

data class AuthRequest(
    val username: String?,
    val email: String?,
    val password: String
)
