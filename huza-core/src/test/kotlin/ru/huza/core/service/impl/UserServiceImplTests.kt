package ru.huza.core.service.impl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import ru.huza.core.exception.NotFoundException
import ru.huza.core.model.dto.UserPatchModel
import ru.huza.core.model.dto.UserSaveModel
import ru.huza.core.service.UserService
import ru.huza.data.dao.UserDao
import ru.huza.data.entity.User

class UserServiceImplTests : ShouldSpec({

    val now = LocalDateTime.now()
    val ownerRoleName = "owner"
    lateinit var userDao: UserDao
    lateinit var userService: UserService

    beforeEach {
        userDao = mockk()
        userService = UserServiceImpl(
            ownerRoleName = ownerRoleName,
            clock = Clock.fixed(now.toInstant(ZoneOffset.UTC), ZoneOffset.UTC),
            userDao = userDao,
        )
    }

    afterEach {
        confirmVerified(userDao)
    }

    should("create() should fill entity fields property") {
        val entityId = 42L
        val entitySlot = slot<User>()

        every { userDao.save(capture(entitySlot)) } answers {
            User(entitySlot.captured)
                .apply { this.id = entityId }
        }

        val savedEntity = userService.create(
            UserSaveModel(
                email = "myEmail",
                username = "myUsername",
                password = "myPassword",
                role = "myRole",
                avatarUrl = "https://pleshka.com",
            )
        )

        savedEntity.should {
            it.email shouldBe "myEmail"
            it.username shouldBe "myUsername"
            it.password shouldBe "myPassword"
            it.role shouldBe "myRole"
            it.avatarUrl shouldBe "https://pleshka.com"
            it.authDate.shouldBeNull()
        }

        verify(exactly = 1) { userDao.save(any()) }

        entitySlot.isCaptured.shouldBeTrue()
        entitySlot.captured.should {
            it.email shouldBe "myEmail"
            it.username shouldBe "myUsername"
            it.password shouldBe "myPassword"
            it.role shouldBe "myRole"
            it.avatarUrl shouldBe "https://pleshka.com"
            it.creationDate shouldBe now
            it.auditDate shouldBe now
            it.version.shouldBeNull()
        }
    }

    should("updateById() should fill entity fields property") {
        val entityId = 42L
        val prevDate = now.minusDays(1)
        val entitySlot = slot<User>()

        every { userDao.findByIdOrNull(eq(entityId)) } answers {
            User().apply {
                this.id = entityId
                this.username = "exUsername"
                this.email = "exEmail"
                this.password = "exPassword"
                this.role = "exRole"
                this.avatarUrl = "exAvatarUrl"
                this.authDate = prevDate
                this.creationDate = prevDate
                this.auditDate = prevDate
                this.version = 1L
            }
        }

        every { userDao.save(capture(entitySlot)) } answers {
            User(entitySlot.captured)
                .apply { this.version = 2L }
        }

        val savedEntity = userService.updateById(
            id = entityId,
            UserSaveModel(
                email = "myEmail",
                username = "myUsername",
                password = "myPassword",
                role = "myRole",
                avatarUrl = "https://pleshka.com",
            )
        )

        savedEntity.should {
            it.email shouldBe "myEmail"
            it.username shouldBe "myUsername"
            it.password shouldBe "myPassword"
            it.role shouldBe "myRole"
            it.avatarUrl shouldBe "https://pleshka.com"
            it.authDate shouldBe prevDate
        }

        verify(exactly = 1) {
            userDao.findByIdOrNull(eq(entityId))
            userDao.save(any())
        }
    }

    should("patchById() should fill entity fields property") {
        val entityId = 42L
        val prevDate = now.minusDays(1)
        val entitySlot = slot<User>()

        every { userDao.findByIdOrNull(eq(entityId)) } answers {
            User().apply {
                this.id = entityId
                this.username = "exUsername"
                this.email = "exEmail"
                this.password = "exPassword"
                this.role = "exRole"
                this.avatarUrl = "exAvatarUrl"
                this.authDate = prevDate
                this.creationDate = prevDate
                this.auditDate = prevDate
                this.version = 1L
            }
        }

        every { userDao.save(capture(entitySlot)) } answers {
            User(entitySlot.captured)
                .apply { this.version = 2L }
        }

        val savedEntity = userService.patchById(
            id = entityId,
            UserPatchModel(
                email = "myEmail",
                username = "myUsername",
                password = "myPassword",
                role = "myRole",
                avatarUrl = "https://pleshka.com",
            )
        )

        savedEntity.should {
            it.email shouldBe "myEmail"
            it.username shouldBe "myUsername"
            it.password shouldBe "myPassword"
            it.role shouldBe "myRole"
            it.avatarUrl shouldBe "https://pleshka.com"
            it.authDate shouldBe prevDate
        }

        verify(exactly = 1) {
            userDao.findByIdOrNull(eq(entityId))
            userDao.save(any())
        }
    }

    context("removeById() method") {

        should("should go on successfully on happy path") {
            val entityId = 42L

            every { userDao.findByIdOrNull(eq(entityId)) } returns mockk { every { role } returns "myRole" }
            every { userDao.findAll() } returns emptyList()
            every { userDao.deleteById(eq(entityId)) } just runs

            userService.removeById(id = entityId).shouldBeTrue()

            verify(exactly = 1) {
                userDao.findByIdOrNull(eq(entityId))
                userDao.findAll()
                userDao.deleteById(eq(entityId))
            }
        }

        should("should return false when entity not found") {
            val entityId = 42L

            every { userDao.findByIdOrNull(entityId) } returns null

            userService.removeById(id = entityId).shouldBeFalse()

            verify(exactly = 1) {
                userDao.findByIdOrNull(eq(entityId))
            }
        }

        should("should throw if trying to delete a single owner") {
            val entityId = 42L

            every { userDao.findByIdOrNull(eq(entityId)) } returns mockk { every { role } returns ownerRoleName }
            every { userDao.findAll() } returns listOf(
                mockk { every { role } returns ownerRoleName }
            )
            every { userDao.deleteById(eq(entityId)) } just runs

            shouldThrow<IllegalStateException> { userService.removeById(id = entityId) }

            verify(exactly = 1) {
                userDao.findByIdOrNull(eq(entityId))
                userDao.findAll()
            }
        }
    }

    context("search methods") {

        val usernames = listOf("username1", "username2")
        val entity1 = User().apply {
            this.id = 1L
            this.email = "email1"
            this.username = usernames.first()
            this.password = "password1"
            this.role = "role1"
            this.avatarUrl = "avatarUrl1"
        }

        should("findByUsernames() delegates to dao.findByUsernameIn()") {
            every { userDao.findByUsernameIn(eq(usernames)) } returns listOf(entity1)
            userService.findByUsernames(usernames)
            verify(exactly = 1) { userDao.findByUsernameIn(eq(usernames)) }
        }

        should("findAll() delegates to dao.findAll()") {
            every { userDao.findAll() } returns listOf(entity1)
            userService.findAll()
            verify(exactly = 1) { userDao.findAll() }
        }

        should("findById() delegates to dao.findByIdOrNull()") {
            val entityId = entity1.id!!
            every { userDao.findByIdOrNull(eq(entityId)) } returns entity1
            userService.findById(entityId)
            verify(exactly = 1) { userDao.findByIdOrNull(eq(entityId)) }
        }

        should("findById() throws NotFoundException if entity not found") {
            val entityId = 42L
            every { userDao.findByIdOrNull(eq(entityId)) } returns null
            shouldThrow<NotFoundException> { userService.findById(entityId) }
            verify(exactly = 1) { userDao.findByIdOrNull(eq(entityId)) }
        }
    }

    should("trackAuth() updates authDate") {
        val entityId = 42L
        val prevDate = now.minusDays(1)
        val entitySlot = slot<User>()

        every { userDao.findByIdOrNull(eq(entityId)) } answers {
            User().apply {
                this.id = entityId
                this.username = "exUsername"
                this.email = "exEmail"
                this.password = "exPassword"
                this.role = "exRole"
                this.avatarUrl = "exAvatarUrl"
                this.authDate = prevDate
                this.creationDate = prevDate
                this.auditDate = prevDate
                this.version = 1L
            }
        }

        every { userDao.save(capture(entitySlot)) } answers {
            User(entitySlot.captured).apply { this.version = 2L }
        }

        userService.trackAuth(entityId)

        entitySlot.isCaptured.shouldBeTrue()
        entitySlot.captured.should {
            it.authDate.shouldBe(now)
        }

        verify(exactly = 1) {
            userDao.findByIdOrNull(eq(entityId))
            userDao.save(any())
        }
    }

    context("implemented methods of UserDetailsService|UserDetailsPasswordService") {

        val entity = User().apply {
            this.id = 42L
            this.email = "myUsername@email.com"
            this.username = "myUsername"
            this.password = "myPassword"
            this.role = "myRole"
            this.avatarUrl = "myAvatarUrl"
        }

        fun verifyFindByLogin(
            login: String,
            usernameFound: Boolean = true,
        ) {
            verify(exactly = 1) {
                userDao.findByUsername(eq(login))
                if (!usernameFound) userDao.findByEmail(eq(login))
            }
        }

        should("loadUserByUsername() go on successfully on happy path") {
            val entityEmail = entity.email!!

            every { userDao.findByUsername(any()) } returns null
            every { userDao.findByEmail(eq(entityEmail)) } returns entity

            val foundUser = userService.loadUserByUsername(entityEmail)

            foundUser.should {
                it.id shouldBe entity.id
                it.email shouldBe entityEmail
            }

            verifyFindByLogin(login = entityEmail, usernameFound = false)
        }

        should("updatePassword() go on successfully on happy path") {
            val entityUsername = entity.username!!
            val userDetails = mockk<UserDetails>() { every { username } returns entityUsername }
            val newPassword = "newPassword"
            val entitySlot = slot<User>()

            every { userDao.findByUsername(eq(entityUsername)) } returns entity
            every { userDao.save(capture(entitySlot)) } answers { User(entitySlot.captured) }

            val foundUser = userService.updatePassword(userDetails, newPassword = newPassword)

            foundUser.should {
                it.id shouldBe entity.id
                it.username shouldBe entityUsername
            }

            verifyFindByLogin(login = entityUsername, usernameFound = true)
            verify(exactly = 1) { userDao.save(any()) }
        }
    }
})
