package ru.huza.misc

import jakarta.annotation.PostConstruct
import java.net.URL
import kotlin.random.Random
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import ru.huza.core.model.dto.AssetDefSaveModel
import ru.huza.core.model.dto.AssetSaveModel
import ru.huza.core.model.dto.FileSaveModel
import ru.huza.core.model.dto.UserDto
import ru.huza.core.model.dto.UserSaveModel
import ru.huza.core.service.AssetDefService
import ru.huza.core.service.AssetService
import ru.huza.core.service.FileService
import ru.huza.core.service.UserService
import ru.huza.data.entity.User

@Component
class DatabaseFiller(
    private val userService: UserService,
    private val assetDefService: AssetDefService,
    private val assetService: AssetService,
    private val fileService: FileService,
) {

    @Value("\${huza.api-url}")
    lateinit var apiUrl: URL

    private val random: Random = Random(seed = 4242)

    @PostConstruct
    fun fillDatabase() {
        createUsers()
        createResources()
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
                cost = listOf(
                    AssetDefSaveModel.CostElement(ResourceCodes.gold, 100),
                    AssetDefSaveModel.CostElement(ResourceCodes.wood, 3),
                    AssetDefSaveModel.CostElement(ResourceCodes.ore, 2),
                ),
            ),
        )

        val villageHall = assetDefService.findByCode("village-hall") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "village-hall",
                name = "Сельская управа",
                description = "Изначально присутствует в каждом городе, ежедневно приносит в казну 500 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/a/a1/Сельская_управа_-_ЗамокH3.png/revision/latest?cb=20170119074045&path-prefix=ru",
                cost = emptyList(),
            ),
        )

        val townHall = assetDefService.findByCode("town-hall") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "town-hall",
                name = "Ратуша",
                description = "Ежедневно приносит в казну 1000 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/7/7a/Ратуша_-_ЗамокH3.png/revision/latest?cb=20170119074056&path-prefix=ru",
                cost = emptyList(),
            ),
        )

        val cityHall = assetDefService.findByCode("city-hall") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "city-hall",
                name = "Магистрат",
                description = "Ежедневно приносит в казну 2000 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/e/e4/Магистрат_-_ЗамокH3.png/revision/latest?cb=20170119074106&path-prefix=ru",
                cost = emptyList(),
            ),
        )

        val capitol = assetDefService.findByCode("capitol") ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.BUILDING.code,
                code = "capitol",
                name = "Капитолий",
                description = "Ежедневно приносит в казну 4000 золотых",
                imgOrigUrl = "https://static.wikia.nocookie.net/heroesofmightandmagic/images/1/17/Капитолий_-_ЗамокH3.png/revision/latest?cb=20170119074117&path-prefix=ru",
                cost = emptyList(),
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
                magicSchool = MagicSchool.FIRE.code,
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

        assetService.create(
            AssetSaveModel(
                assetDefId = tavern.id!!,
                quantity = 1,
            ),
        )
        assetService.create(
            AssetSaveModel(
                assetDefId = villageHall.id!!,
                quantity = 1,
            ),
        )

        assetService.create(
            AssetSaveModel(
                assetDefId = armageddon.id!!,
                quantity = 1,
            ),
        )

        assetService.create(
            AssetSaveModel(
                assetDefId = archangel.id!!,
                quantity = 10,
            ),
        )
    }

    private fun createResources() {

        fun getImageLink(resourceCode: String): String {
            val fileId = "resource_$resourceCode"

            fileService.findByIdOrNull(fileId) ?: fileService.createInternal(
                id = fileId,
                model = FileSaveModel(
                    resource = ClassPathResource("img/$fileId.png"),
                    fileName = "$fileId.png",
                    mediaType = "image/png",
                ),
            )

            return apiUrl.toURI()
                .resolve("files/download/$fileId")
                .toURL()
                .toString()
        }

        val gold = assetDefService.findByCode(ResourceCodes.gold) ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = ResourceCodes.gold,
                name = "Золото",
                description = "Золото (англ. Gold) – важнейший ресурс, присутствующий во всех играх серии Герои Меча и Магии, а также валюта в серии Меч и Магия.",
                imgOrigUrl = getImageLink(ResourceCodes.gold),
            ),
        )
        val ore = assetDefService.findByCode(ResourceCodes.ore) ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = ResourceCodes.ore,
                name = "Руда",
                description = "Руда (англ. Ore) - один из трёх главных ресурсов во всех играх серии Герои Меча и Магии, чаще всего используемых при строительстве (наряду с золотом и древесиной). Основным источником добычи являются рудные шахты, приносящие 2 ед. руды в день.",
                imgOrigUrl = getImageLink(ResourceCodes.ore),
            ),
        )
        val wood = assetDefService.findByCode(ResourceCodes.wood) ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = ResourceCodes.wood,
                name = "Древесина",
                description = "Древесина (англ. Wood) – один из трёх главных ресурсов во всех играх серии Герои Меча и Магии, чаще всего используемых при строительстве (наряду с золотом и рудой). Основным источником добычи являются лесопилки, приносящие 2 ед. древесины в день.",
                imgOrigUrl = getImageLink(ResourceCodes.wood),
            ),
        )
        val mercury = assetDefService.findByCode(ResourceCodes.mercury) ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = ResourceCodes.mercury,
                name = "Ртуть",
                description = "Ртуть (англ. Mercury) - ценный ресурс в серии игр Герои Меча и Магии. Он весьма редок, необходим для постройки и совершенствования высокоуровневых зданий и Гильдий Магов. Также ртуть потребуется для найма некоторых мощных юнитов.",
                imgOrigUrl = getImageLink(ResourceCodes.mercury),
            ),
        )
        val sulfur = assetDefService.findByCode(ResourceCodes.sulfur) ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = ResourceCodes.sulfur,
                name = "Сера",
                description = "Руда (англ. Ore) - один из трёх главных ресурсов во всех играх серии Герои Меча и Магии, чаще всего используемых при строительстве (наряду с золотом и древесиной). Основным источником добычи являются рудные шахты, приносящие 2 ед. руды в день.",
                imgOrigUrl = getImageLink(ResourceCodes.sulfur),
            ),
        )
        val crystal = assetDefService.findByCode(ResourceCodes.crystal) ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = ResourceCodes.crystal,
                name = "Кристаллы",
                description = "Кристаллы (англ. Crystal) – ценный ресурс в серии игр Герои Меча и Магии. Он весьма редок, необходим для постройки и совершенствования высокоуровневых зданий и Гильдий Магов. Также кристаллы потребуются для найма некоторых мощных юнитов.",
                imgOrigUrl = getImageLink(ResourceCodes.crystal),
            ),
        )
        val gem = assetDefService.findByCode(ResourceCodes.gem) ?: assetDefService.create(
            AssetDefSaveModel(
                type = AssetDefType.RESOURCE.code,
                code = ResourceCodes.gem,
                name = "Самоцветы",
                description = "Самоцветы (англ. Gem) – ценный ресурс в серии игр Герои Меча и Магии. Он весьма редок, необходим для постройки и совершенствования высокоуровневых зданий и гильдий магов. Также самоцветы потребуются для найма некоторых мощных юнитов. ",
                imgOrigUrl = getImageLink(ResourceCodes.gem),
            ),
        )

        // Create assets
        assetService.create(
            AssetSaveModel(
                assetDefId = gold.id!!,
                quantity = 1488,
            ),
        )
        assetService.create(
            AssetSaveModel(
                assetDefId = ore.id!!,
                quantity = 0,
            ),
        )
        assetService.create(
            AssetSaveModel(
                assetDefId = wood.id!!,
                quantity = 32,
            ),
        )
        assetService.create(
            AssetSaveModel(
                assetDefId = mercury.id!!,
                quantity = 3,
            ),
        )
        assetService.create(
            AssetSaveModel(
                assetDefId = sulfur.id!!,
                quantity = 0,
            ),
        )
        assetService.create(
            AssetSaveModel(
                assetDefId = crystal.id!!,
                quantity = 0,
            ),
        )
        assetService.create(
            AssetSaveModel(
                assetDefId = gem.id!!,
                quantity = 0,
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
                UserSaveModel(
                    email = it.email,
                    username = it.username,
                    password = it.password,
                    role = it.role,
                    avatarUrl = "https://www.gravatar.com/avatar/1ca58d0a4a10ea07fabd4e4d32968982?s=256&d=identicon&r=PG",
                )
            }
            .map(userService::create)
            .onEach { LOGGER.info(it.toString()) }
            .toList()
    }

    companion object {

        private val LOGGER: Logger = LoggerFactory.getLogger(DatabaseFiller::class.java)

        // Decrypted version: 'password'
        // https://bcrypt-generator.com/
        private const val BCRYPTED_PASSWORD = "\$2a\$12\$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G"
    }
}
