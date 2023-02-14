package ru.huza.misc

import jakarta.annotation.PostConstruct
import kotlin.random.Random
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.huza.core.model.dto.AssetDefDto
import ru.huza.core.model.dto.AssetDefSaveModel
import ru.huza.core.model.dto.AssetDto
import ru.huza.core.model.dto.UserDto
import ru.huza.core.model.dto.toLink
import ru.huza.core.service.AssetDefService
import ru.huza.core.service.AssetService
import ru.huza.core.service.UserService
import ru.huza.data.entity.User

@Component
class DatabaseFiller(
    private val userService: UserService,
    private val assetDefService: AssetDefService,
    private val assetService: AssetService
) {

    private val random: Random = Random(seed = 4242)

    @PostConstruct
    fun fillDatabase() {
        createUsers()
        createAssetDefs()
    }

    private fun createAssetDefs() {
        // ===== BUILDINGS =====
        val tavern = assetDefService.findByCode("tavern") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "tavern",
                name = "Таверна",
                description = "Позволяет нанимать героев, а также повышает боевой дух войск гарнизона на 1 ед",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/a/ad/Таверна_-_ЗамокH3.png/revision/latest?cb=20170119074127&path-prefix=ru",
            ),
        )

        val villageHall = assetDefService.findByCode("village-hall") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "village-hall",
                name = "Сельская управа",
                description = "Изначально присутствует в каждом городе, ежедневно приносит в казну 500 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/a/a1/Сельская_управа_-_ЗамокH3.png/revision/latest?cb=20170119074045&path-prefix=ru",
            ),
        )

        val townHall = assetDefService.findByCode("town-hall") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "town-hall",
                name = "Ратуша",
                description = "Ежедневно приносит в казну 1000 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/7/7a/Ратуша_-_ЗамокH3.png/revision/latest?cb=20170119074056&path-prefix=ru",
            ),
        )

        val cityHall = assetDefService.findByCode("city-hall") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "city-hall",
                name = "Магистрат",
                description = "Ежедневно приносит в казну 2000 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/e/e4/Магистрат_-_ЗамокH3.png/revision/latest?cb=20170119074106&path-prefix=ru",
            ),
        )

        val capitol = assetDefService.findByCode("capitol") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "capitol",
                name = "Капитолий",
                description = "Ежедневно приносит в казну 4000 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/1/17/Капитолий_-_ЗамокH3.png/revision/latest?cb=20170119074117&path-prefix=ru",
            ),
        )

        // ===== SPELLS =====
        val armageddon = assetDefService.findByCode("armageddon") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.SPELL.code,
                code = "armageddon",
                name = "Армагедон",
                description = "Armageddon is a 4th level spell in the School of Fire Magic. It damages all creatures on the battlefield, including the caster's own troops. However, a few creatures can resist the spell due to their spell immunities.",
                imgOrigUrl = "https://heroes.thelazy.net/images/archive/6/63/20170316101406%21Armageddon.png",
            ),
        )

        // ===== RESOURCES =====
        val gold = assetDefService.findByCode("gold") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = "gold",
                name = "Золото",
                description = "Золото (ориг. Gold) – важнейший ресурс, присутствующий во всех играх серии Герои Меча и Магии, а также валюта в серии Меч и Магия.",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/9/9b/Золото_-_H3.jpg/revision/latest?cb=20160419123135&path-prefix=ru",
            ),
        )
        val wood = assetDefService.findByCode("wood") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = "wood",
                name = "Древесина",
                description = "Древесина (англ. Wood) – один из трёх главных ресурсов во всех играх серии Герои Меча и Магии, чаще всего используемых при строительстве (наряду с золотом и рудой). Основным источником добычи являются лесопилки, приносящие 2 ед. древесины в день.",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/e/e3/Древесина_-_H5.jpg/revision/latest?cb=20170626161819&path-prefix=ru",
            ),
        )
        val mercury = assetDefService.findByCode("mercury") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = "mercury",
                name = "Ртуть",
                description = "Ртуть (англ. Mercury) - ценный ресурс в серии игр Герои Меча и Магии. Он весьма редок, необходим для постройки и совершенствования высокоуровневых зданий и Гильдий Магов. Также ртуть потребуется для найма некоторых мощных юнитов.",
                imgOrigUrl = "https://s00.yaplakal.com/pics/pics_original/9/5/1/4377159.jpg",
            ),
        )

        // ===== RECRUITS =====
        val archangel = assetDefService.findByCode("archangel") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RECRUIT.code,
                code = "archangel",
                name = "Архангел",
                description = "Позволяет нанимать героев, а также повышает боевой дух войск гарнизона на 1 ед",
                imgOrigUrl = "https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg",
            ),
        )

        assetService.save(
            AssetDto(
                assetDef = tavern.toLink(),
                quantity = 1,
            ),
        )
        assetService.save(
            AssetDto(
                assetDef = villageHall.toLink(),
                quantity = 1,
            ),
        )

        assetService.save(
            AssetDto(
                assetDef = armageddon.toLink(),
                quantity = 1,
            ),
        )

        assetService.save(
            AssetDto(
                assetDef = gold.toLink(),
                quantity = 1488,
            ),
        )
        assetService.save(
            AssetDto(
                assetDef = wood.toLink(),
                quantity = 32,
            ),
        )
        assetService.save(
            AssetDto(
                assetDef = mercury.toLink(),
                quantity = 3,
            ),
        )

        assetService.save(
            AssetDto(
                assetDef = archangel.toLink(),
                quantity = 10,
            ),
        )
    }

    private fun createUsers(): List<UserDto> {
        val usersToCreate = UserRole.values()
            .map(UserRole::code)
            .map { role ->
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
        // private const val BCRYPTED_PASSWORD = "\$2a\$10\$qMEoXE2zE1UWyH2kqFBuP.MJzYsL4/LHJw4RXl5rD50he6S3tB9ES"

        private val ROLES: List<String> = UserRole.values().map(UserRole::code)
    }
}
