package ru.huza.model.response

data class AuthInfoResponse(

    val username: String,

    val roles: List<String>
)
