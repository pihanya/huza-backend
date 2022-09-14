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
        createAssetDefs()
    }

    private fun createAssetDefs() {
        var assetDefCtr = 100000L

        // ===== BUILDINGS =====
        val tavern = assetDefService.findByCode("tavern") ?: assetDefService.save(
            AssetDefDto(
                id = assetDefCtr++,
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
                id = assetDefCtr++,
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
                id = assetDefCtr++,
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
                id = assetDefCtr++,
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
                id = assetDefCtr++,
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

        // ===== SPELLS =====
        val armageddon = assetDefService.findByCode("armageddon") ?: assetDefService.save(
            AssetDefDto(
                id = assetDefCtr++,
                type = "SPELL",
                code = "armageddon",
                name = "Армагедон",
                description = "Armageddon is a 4th level spell in the School of Fire Magic. It damages all creatures on the battlefield, including the caster's own troops. However, a few creatures can resist the spell due to their spell immunities.",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://heroes.thelazy.net/images/archive/6/63/20170316101406%21Armageddon.png"
            )
        )

        // ===== RESOURCES =====
        val gold = assetDefService.findByCode("gold") ?: assetDefService.save(
            AssetDefDto(
                id = assetDefCtr++,
                type = "RESOURCE",
                code = "gold",
                name = "Золото",
                description = "Золото (ориг. Gold) – важнейший ресурс, присутствующий во всех играх серии Герои Меча и Магии, а также валюта в серии Меч и Магия.",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/9/9b/Золото_-_H3.jpg/revision/latest?cb=20160419123135&path-prefix=ru"
            )
        )
        val wood = assetDefService.findByCode("wood") ?: assetDefService.save(
            AssetDefDto(
                id = assetDefCtr++,
                type = "RESOURCE",
                code = "wood",
                name = "Древесина",
                description = "Древесина (англ. Wood) – один из трёх главных ресурсов во всех играх серии Герои Меча и Магии, чаще всего используемых при строительстве (наряду с золотом и рудой). Основным источником добычи являются лесопилки, приносящие 2 ед. древесины в день.",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/e/e3/Древесина_-_H5.jpg/revision/latest?cb=20170626161819&path-prefix=ru"
            )
        )
        val mercury = assetDefService.findByCode("mercury") ?: assetDefService.save(
            AssetDefDto(
                id = assetDefCtr++,
                type = "RESOURCE",
                code = "mercury",
                name = "Ртуть",
                description = "Ртуть (англ. Mercury) - ценный ресурс в серии игр Герои Меча и Магии. Он весьма редок, необходим для постройки и совершенствования высокоуровневых зданий и Гильдий Магов. Также ртуть потребуется для найма некоторых мощных юнитов.",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://s00.yaplakal.com/pics/pics_original/9/5/1/4377159.jpg"
            )
        )

        // ===== RECRUITS =====
        val archangel = assetDefService.findByCode("archangel") ?: assetDefService.save(
            AssetDefDto(
                id = assetDefCtr++,
                type = "RECRUIT",
                code = "archangel",
                name = "Архангел",
                description = "Позволяет нанимать героев, а также повышает боевой дух войск гарнизона на 1 ед",
                img75Url = null,
                img130Url = null,
                img250Url = null,
                imgOrigUrl = "https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg"
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

        assetService.save(
            AssetDto(
                assetDef = armageddon.toLink(),
                quantity = 1
            )
        )

        assetService.save(
            AssetDto(
                assetDef = gold.toLink(),
                quantity = 1488
            )
        )
        assetService.save(
            AssetDto(
                assetDef = wood.toLink(),
                quantity = 32
            )
        )
        assetService.save(
            AssetDto(
                assetDef = mercury.toLink(),
                quantity = 3
            )
        )

        assetService.save(
            AssetDto(
                assetDef = archangel.toLink(),
                quantity = 10
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
