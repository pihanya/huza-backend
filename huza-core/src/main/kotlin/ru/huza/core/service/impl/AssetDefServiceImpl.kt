package ru.huza.core.service.impl

import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.model.dto.AssetDefDto
import ru.huza.core.model.dto.AssetDefPatchModel
import ru.huza.core.model.dto.AssetDefSaveModel
import ru.huza.core.service.AssetDefService
import ru.huza.data.dao.AssetDefDao
import ru.huza.data.entity.AssetDef

@Service
class AssetDefServiceImpl : AssetDefService {

    @set:Autowired
    lateinit var assetDefDao: AssetDefDao

    @Transactional
    override fun create(model: AssetDefSaveModel): AssetDefDto {
        val now = LocalDateTime.now()

        val entityToSave = fillFromSaveModel(existingEntity = null, saveModel = model, now = now)
        return assetDefDao.save(entityToSave).let(::toDto)
    }

    @Transactional
    override fun updateById(id: Long, model: AssetDefSaveModel): AssetDefDto {
        val now = LocalDateTime.now()

        val existingEntity = assetDefDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset Def with id [$id] does not exist" }

        val updatedEntity = fillFromSaveModel(existingEntity = existingEntity, saveModel = model, now = now)
        return assetDefDao.save(updatedEntity).let(::toDto)
    }

    @Transactional
    override fun patchById(id: Long, model: AssetDefPatchModel): AssetDefDto {
        val now = LocalDateTime.now()

        val existingEntity = assetDefDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset Def with id [$id] does not exist" }

        val updatedEntity = fillFromPatchModel(existingEntity = existingEntity, patchModel = model, now = now)
        return assetDefDao.save(updatedEntity).let(::toDto)
    }

    override fun findById(id: Long): AssetDefDto =
        assetDefDao.findById(id).map(::toDto).orElseThrow()

    override fun findByCode(code: String): AssetDefDto? =
        assetDefDao.findByCode(code)?.let(::toDto)

    override fun findAll(): List<AssetDefDto> =
        assetDefDao.findAll().asSequence()
            .map(::toDto)
            .sortedBy(AssetDefDto::code)
            .toList()

    private fun toDto(entity: AssetDef): AssetDefDto = AssetDefDto(
        id = entity.id,
        type = entity.type!!,
        code = entity.code!!,
        name = entity.name!!,
        description = entity.description,
        imgOrigUrl = entity.imgOrigUrl,
        creationDate = entity.creationDate ?: error("creationDate was null for entity [${entity.id}]"),
        auditDate = entity.auditDate ?: error("auditDate was null for entity [${entity.id}]"),
    )

    private fun fillFromSaveModel(
        existingEntity: AssetDef? = null,
        saveModel: AssetDefSaveModel,
        now: LocalDateTime,
    ): AssetDef =
        AssetDef().apply {
            this.id = existingEntity?.id
            this.type = saveModel.type ?: existingEntity?.type ?: error("type was null")
            this.code = saveModel.code ?: existingEntity?.code ?: error("code was null")
            this.name = saveModel.name ?: existingEntity?.name ?: error("name was null")
            this.description = saveModel.description ?: existingEntity?.description
            this.imgOrigUrl = saveModel.imgOrigUrl ?: existingEntity?.imgOrigUrl
            this.creationDate = existingEntity?.creationDate ?: now
            this.auditDate = now
            existingEntity?.version?.let { this.version = it }
        }

    private fun fillFromPatchModel(
        existingEntity: AssetDef,
        patchModel: AssetDefPatchModel,
        now: LocalDateTime,
    ): AssetDef =
        AssetDef(existingEntity).apply {
            this.type = patchModel.type ?: this.type
            this.code = patchModel.code ?: this.code
            this.name = patchModel.name ?: this.name
            this.description = patchModel.description ?: this.description
            this.imgOrigUrl = patchModel.imgOrigUrl ?: this.imgOrigUrl
            this.auditDate = now
            existingEntity.version?.let { this.version = it }
        }
}
