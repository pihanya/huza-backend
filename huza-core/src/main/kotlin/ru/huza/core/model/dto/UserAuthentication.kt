package ru.huza.core.model.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UserAuthentication : UsernamePasswordAuthenticationToken {

    val email: String

    constructor(
        principal: Any,
        credentials: Any?,
        email: String
    ) : super(principal, credentials) {
        this.email = email
    }

    constructor(
        principal: Any,
        credentials: Any?,
        authorities: List<GrantedAuthority>,
        email: String
    ) : super(principal, credentials, authorities) {
        this.email = email
    }
}
