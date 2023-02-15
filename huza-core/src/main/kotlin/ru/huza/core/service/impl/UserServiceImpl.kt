package ru.huza.core.service.impl

import java.time.LocalDateTime
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.exception.NotFoundException
import ru.huza.core.model.dto.UserDto
import ru.huza.core.model.dto.UserPatchModel
import ru.huza.core.model.dto.UserSaveModel
import ru.huza.core.service.UserService
import ru.huza.data.dao.UserDao
import ru.huza.data.entity.User

private val logger = KotlinLogging.logger {}

@Service
class UserServiceImpl : UserService {

    @set:Autowired
    lateinit var userDao: UserDao

    @Value("\${huza.owner-role-name}")
    lateinit var ownerRoleName: String
    
    @Transactional
    override fun create(model: UserSaveModel): UserDto {
        val now = LocalDateTime.now()

        val entityToSave = fillFromSaveModel(existingEntity = null, saveModel = model, now = now)
        return userDao.save(entityToSave).let(::toDto)
    }

    @Transactional
    override fun updateById(id: Long, model: UserSaveModel): UserDto {
        val now = LocalDateTime.now()

        val existingEntity = userDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset with id [$id] does not exist" }

        val updatedEntity = fillFromSaveModel(existingEntity = existingEntity, saveModel = model, now = now)
        return userDao.save(updatedEntity).let(::toDto)
    }

    @Transactional
    override fun patchById(id: Long, model: UserPatchModel): UserDto {
        val now = LocalDateTime.now()

        val existingEntity = userDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset with id [$id] does not exist" }

        val updatedEntity = fillFromPatchModel(existingEntity = existingEntity, patchModel = model, now = now)
        return userDao.save(updatedEntity).let(::toDto)
    }

    @Transactional
    override fun removeById(id: Long): Boolean {
        val existingUser = userDao.findByIdOrNull(id) ?: return false

        val owners = userDao.findAll().filter { it.role == ownerRoleName }
        if (existingUser.role == ownerRoleName && owners.count() <= 1) {
            error("Нельзя удалить единственного владельца замка!")
        }

        userDao.deleteById(id)
        return true
    }

    override fun findByUsernames(usernames: List<String>): List<UserDto> =
        userDao.findByUsernameIn(usernames)
            .map(::toDto)

    override fun findAll(): List<UserDto> =
        userDao.findAll().map(::toDto)

    override fun findById(id: Long): UserDto {
        val existingEntity = userDao.findByIdOrNull(id)
            ?: throw NotFoundException("Service User with id [$id] does not exist")
        return existingEntity.let(::toDto)
    }

    override fun trackAuth(id: Long) {
        val now = LocalDateTime.now()

        val existingEntity = userDao.findByIdOrNull(id)
            ?.apply { this.authDate = now }
            ?: throw NotFoundException("Service User with id [$id] does not exist")

        userDao.save(existingEntity)
        logger.debug { "Tracked authentication of user [$id] at [$now]" }
    }

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
            id = entity.id!!,
            email = entity.email!!,
            username = entity.username!!,
            password = entity.password!!,
            role = entity.role!!,
            avatarUrl = entity.avatarUrl!!,
            authDate = entity.authDate,
        )

    private fun fillFromSaveModel(
        existingEntity: User? = null,
        saveModel: UserSaveModel,
        now: LocalDateTime,
    ): User =
        User().apply {
            this.id = existingEntity?.id
            this.username = saveModel.username ?: existingEntity?.username
            this.email = saveModel.email ?: existingEntity?.email ?: error("email was null")
            this.password = saveModel.password ?: existingEntity?.password ?: error("password was null")
            this.role = saveModel.role ?: existingEntity?.role ?: error("role was null")
            this.avatarUrl = saveModel.avatarUrl ?: existingEntity?.avatarUrl ?: error("avatarUrl was null")
            this.authDate = existingEntity?.authDate
            this.creationDate = existingEntity?.creationDate ?: now
            this.auditDate = now
            existingEntity?.version?.let { this.version = it }
        }

    private fun fillFromPatchModel(
        existingEntity: User,
        patchModel: UserPatchModel,
        now: LocalDateTime,
    ): User =
        User(existingEntity).apply {
            this.email = patchModel.email ?: existingEntity.email
            this.username = patchModel.username ?: existingEntity.username
            this.password = patchModel.password ?: existingEntity.password
            this.role = patchModel.role ?: existingEntity.role
            this.avatarUrl = patchModel.avatarUrl ?: existingEntity.avatarUrl
            this.auditDate = now
            existingEntity.version?.let { this.version = it }
        }
}
