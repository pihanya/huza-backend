package ru.huza.misc

import jakarta.annotation.PostConstruct
import kotlin.random.Random
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.huza.dto.AssetDefDto
import ru.huza.dto.AssetDto
import ru.huza.dto.UserDto
import ru.huza.dto.toLink
import ru.huza.entity.User
import ru.huza.service.AssetDefService
import ru.huza.service.AssetService
import ru.huza.service.UserService

@Component
class DatabaseFiller(
    private val userService: UserService,
    private val assetDefService: AssetDefService,
    private val assetService: AssetService
) {

    private val random: Random = Random(1428)

    @PostConstruct
    fun fillDatabase() {
        createUsers()

        // "SPELL" — заклинание
        // "RESOURCE" — ресурс
        // "RECRUIT" — рекрут

        val tavern = assetDefService.findByCode("tavern") ?: assetDefService.save(
            AssetDefDto(
                id = 1,
                type = "BUILDING",
                code = "tavern",
                name = "Таверна",
                description = "Позволяет нанимать героев, а также повышает боевой дух войск гарнизона на 1 ед",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/a/ad/Таверна_-_ЗамокH3.png/revision/latest?cb=20170119074127&path-prefix=ru"
            )
        )

        val villageHall = assetDefService.findByCode("village-hall") ?: assetDefService.save(
            AssetDefDto(
                id = 2,
                type = "BUILDING",
                code = "village-hall",
                name = "Сельская управа",
                description = "Изначально присутствует в каждом городе, ежедневно приносит в казну 500 золотых",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/a/a1/Сельская_управа_-_ЗамокH3.png/revision/latest?cb=20170119074045&path-prefix=ru"
            )
        )

        val townHall = assetDefService.findByCode("town-hall") ?: assetDefService.save(
            AssetDefDto(
                id = 3,
                type = "BUILDING",
                code = "town-hall",
                name = "Ратуша",
                description = "Ежедневно приносит в казну 1000 золотых",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/7/7a/Ратуша_-_ЗамокH3.png/revision/latest?cb=20170119074056&path-prefix=ru"
            )
        )

        val cityHall = assetDefService.findByCode("city-hall") ?: assetDefService.save(
            AssetDefDto(
                id = 4,
                type = "BUILDING",
                code = "city-hall",
                name = "Магистрат",
                description = "Ежедневно приносит в казну 2000 золотых",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/e/e4/Магистрат_-_ЗамокH3.png/revision/latest?cb=20170119074106&path-prefix=ru"
            )
        )

        val capitol = assetDefService.findByCode("capitol") ?: assetDefService.save(
            AssetDefDto(
                id = 5,
                type = "BUILDING",
                code = "capitol",
                name = "Капитолий",
                description = "Ежедневно приносит в казну 4000 золотых",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/1/17/Капитолий_-_ЗамокH3.png/revision/latest?cb=20170119074117&path-prefix=ru"
            )
        )

        assetService.save(
            AssetDto(
                assetDef = tavern.toLink(),
                quantity = 1
            )
        )
        assetService.save(
            AssetDto(
                assetDef = villageHall.toLink(),
                quantity = 1
            )
        )
    }

    private fun createUsers(): List<UserDto> {
        val usersToCreate = ROLES.map { role ->
            User().apply {
                this.username = role
                this.email = "$role@itmo.ru"
                this.password = BCRYPTED_PASSWORD
                this.role = role
            }
        }

        val existingUsernames = userService
            .findByUsernames(usersToCreate.mapNotNull(User::username))
            .map(UserDto::getUsername)
            .toSet()

        return usersToCreate.asSequence()
            .filter { it.username !in existingUsernames }
            .map {
                UserDto().apply {
                    this.email = it.email
                    setUsername(it.username!!)
                    setPassword(it.password!!)
                    this.role = it.role
                }
            }
            .map(userService::save)
            .onEach { LOGGER.info(it.toString()) }
            .toList()
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
