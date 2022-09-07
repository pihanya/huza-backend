package ru.huza.dao

import org.springframework.data.repository.CrudRepository
import ru.huza.entity.User

interface UserDao : CrudRepository<User, Long> {

    fun findByEmail(email: String): User?
}
