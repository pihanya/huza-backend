package ru.huza.api.model.response

data class AuthInfoResponse(

    val username: String,

    val roles: List<String>,
)
