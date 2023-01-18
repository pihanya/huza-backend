package ru.huza.core.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import ru.huza.core.model.dto.UserDto

interface UserService : UserDetailsService, UserDetailsPasswordService {

    fun save(entity: UserDto): UserDto

    fun removeById(id: Long): Boolean

    fun findByUsernames(usernames: List<String>): List<UserDto>

    fun findAll(): List<UserDto>

    fun findById(id: Long): UserDto

    override fun loadUserByUsername(username: String): UserDto

    override fun updatePassword(user: UserDetails, newPassword: String): UserDto
}
