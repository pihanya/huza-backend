package ru.huza.dao

import org.springframework.data.repository.CrudRepository
import ru.huza.entity.User

interface UserDao : CrudRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun findByUsernameIn(usernames: List<String>): List<User>

    fun findByEmail(email: String): User?
}
