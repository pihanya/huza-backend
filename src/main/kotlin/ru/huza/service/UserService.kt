package ru.huza.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import ru.huza.dto.UserDto

interface UserService : UserDetailsService, UserDetailsPasswordService {

    fun save(entity: UserDto): UserDto

    fun removeById(id: Long): Boolean

    fun findAll(): List<UserDto>

    fun findById(id: Long): UserDto

    override fun loadUserByUsername(username: String): UserDto

    override fun updatePassword(user: UserDetails, newPassword: String): UserDto
}
