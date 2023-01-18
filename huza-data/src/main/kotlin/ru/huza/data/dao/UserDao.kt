package ru.huza.data.dao

import org.springframework.data.repository.CrudRepository
import ru.huza.data.entity.User

interface UserDao : CrudRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun findByUsernameIn(usernames: List<String>): List<User>

    fun findByEmail(email: String): User?
}
