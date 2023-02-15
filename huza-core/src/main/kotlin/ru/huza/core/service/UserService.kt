package ru.huza.core.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import ru.huza.core.model.dto.UserDto
import ru.huza.core.model.dto.UserPatchModel
import ru.huza.core.model.dto.UserSaveModel

interface UserService : UserDetailsService, UserDetailsPasswordService {

    fun create(model: UserSaveModel): UserDto

    fun updateById(id: Long, model: UserSaveModel): UserDto

    fun patchById(id: Long, model: UserPatchModel): UserDto

    fun removeById(id: Long): Boolean

    fun findByUsernames(usernames: List<String>): List<UserDto>

    fun findAll(): List<UserDto>

    fun findById(id: Long): UserDto

    fun trackAuth(id: Long)

    override fun loadUserByUsername(username: String): UserDto

    override fun updatePassword(user: UserDetails, newPassword: String): UserDto
}
