package ru.huza.misc

import jakarta.annotation.PostConstruct
import kotlin.random.Random
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.huza.dao.UserDao
import ru.huza.entity.User

@Suppress("UNUSED_VARIABLE")
@Component
class DatabaseFiller(
    private val userDao: UserDao
) {

    private val random: Random = Random(1428)

    @PostConstruct
    fun fillDatabase() {
        ROLES
            .map { role ->
                User().apply {
                    this.username = role
                    this.email = "$role@itmo.ru"
                    // this.password = "{noop}password"
                    this.password = BCRYPTED_PASSWORD
                    this.role = ROLES[random.nextInt(0, ROLES.size)]
                }
            }
            .onEach { userDao.save(it) }
            .onEach { LOGGER.info(it.toString()) }
    }

    companion object {

        private val LOGGER: Logger = LoggerFactory.getLogger(DatabaseFiller::class.java)

        // Decrypted version: 'password'
        // https://bcrypt-generator.com/
        private const val BCRYPTED_PASSWORD = "\$2a\$12\$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G"

        private val ROLES: List<String> = listOf(
            "admin",
            "owner",
            "builder",
            "warrior",
            "wizard"
        )
    }
}
