package ru.huza.api.model.request

data class UserSaveModel(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val role: String? = null,
)
