package ru.huza.core.model.dto

import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDto : UserDetails, CredentialsContainer {

    var id: Long? = null

    var email: String? = null

    private var username: String? = null

    private var password: String? = null

    var role: String? = null

    constructor()

    constructor(id: Long, email: String, username: String, password: String, role: String) {
        this.id = id
        this.email = email
        this.username = username
        this.password = password
        this.role = role
    }

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(
            SimpleGrantedAuthority(role)
        )

    override fun getPassword(): String? = password

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getUsername(): String? = username

    fun setUsername(username: String) {
        this.username = username
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun eraseCredentials() {
        this.password = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int =
        id?.hashCode() ?: 0

    override fun toString(): String =
        "UserDto(id=$id, email=$email, username=$username, password=$password, role=$role)"
}
