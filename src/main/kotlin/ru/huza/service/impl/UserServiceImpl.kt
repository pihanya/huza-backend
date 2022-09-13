package ru.huza.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.dao.UserDao
import ru.huza.dto.UserDto
import ru.huza.entity.User
import ru.huza.service.UserService

@Service
class UserServiceImpl : UserService {

    @set:Autowired
    lateinit var userDao: UserDao

    @Transactional
    override fun save(entity: UserDto): UserDto =
        userDao.save(
            User().apply {
                this.id = entity.id
                this.email = entity.email
                this.username = entity.username
                this.password = entity.password
                this.role = entity.role
            }
        ).let(::toDto)

    @Transactional
    override fun removeById(id: Long): Boolean {
        val existed = userDao.existsById(id)
        userDao.deleteById(id)
        return existed
    }

    override fun findByUsernames(usernames: List<String>): List<UserDto> =
        userDao.findByUsernameIn(usernames)
            .map(::toDto)

    override fun findAll(): List<UserDto> =
        userDao.findAll().map(::toDto)

    override fun findById(id: Long): UserDto =
        userDao.findById(id).map(::toDto).orElseThrow()

    @Transactional
    override fun loadUserByUsername(username: String): UserDto {
        val foundUser = findByUsernameOrEmail(username)
        return toDto(foundUser)
    }

    @Transactional
    override fun updatePassword(user: UserDetails, newPassword: String): UserDto {
        val foundUser = findByUsernameOrEmail(user.username)
        return userDao.save(foundUser.apply { this.password = newPassword })
            .let(::toDto)
    }

    private fun findByUsernameOrEmail(username: String) =
        userDao.findByUsername(username)
            ?: userDao.findByEmail(email = username)
            ?: throw UsernameNotFoundException("User [$username] was not found")

    private fun toDto(entity: User): UserDto =
        UserDto(
            entity.id!!,
            entity.email!!,
            entity.username!!,
            entity.password!!,
            entity.role!!
        )
}
