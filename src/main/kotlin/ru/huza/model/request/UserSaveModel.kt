package ru.huza.model.request

data class UserSaveModel(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val role: String? = null
)
