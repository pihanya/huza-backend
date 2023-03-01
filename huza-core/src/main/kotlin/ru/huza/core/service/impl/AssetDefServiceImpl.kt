package ru.huza.core.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.exception.NotFoundException
import ru.huza.core.model.dto.AssetDefDto
import ru.huza.core.model.dto.AssetDefPatchModel
import ru.huza.core.model.dto.AssetDefSaveModel
import ru.huza.core.util.toDto
import ru.huza.core.service.AssetDefService
import ru.huza.data.dao.AssetDefDao
import ru.huza.data.entity.AssetDef

@Service
class AssetDefServiceImpl @Autowired constructor(
    private val assetDefDao: AssetDefDao
) : AssetDefService {

    @Transactional
    override fun create(model: AssetDefSaveModel): AssetDefDto {
        val now = LocalDateTime.now()

        val entityToSave = fillFromSaveModel(existingEntity = null, saveModel = model, now = now)
        return assetDefDao.save(entityToSave).let(AssetDef::toDto)
    }

    @Transactional
    override fun updateById(id: Long, model: AssetDefSaveModel): AssetDefDto {
        val now = LocalDateTime.now()

        val existingEntity = assetDefDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset Def with id [$id] does not exist" }

        val updatedEntity = fillFromSaveModel(existingEntity = existingEntity, saveModel = model, now = now)
        return assetDefDao.save(updatedEntity).let(AssetDef::toDto)
    }

    @Transactional
    override fun patchById(id: Long, model: AssetDefPatchModel): AssetDefDto {
        val now = LocalDateTime.now()

        val existingEntity = assetDefDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset Def with id [$id] does not exist" }

        val updatedEntity = fillFromPatchModel(existingEntity = existingEntity, patchModel = model, now = now)
        return assetDefDao.save(updatedEntity).let(AssetDef::toDto)
    }

    override fun findById(id: Long): AssetDefDto =
        assetDefDao.findByIdOrNull(id)?.let(AssetDef::toDto)
            ?: throw NotFoundException("Asset Def with id [$id] does not exist")

    override fun findByCode(code: String): AssetDefDto? =
        assetDefDao.findByCode(code)?.let(AssetDef::toDto)

    override fun findAll(): List<AssetDefDto> =
        assetDefDao.findAll().asSequence()
            .map(AssetDef::toDto)
            .sortedBy(AssetDefDto::code)
            .toList()

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
            this.cost = toEntityCost(saveModel.cost) ?: existingEntity?.cost
            this.fraction = saveModel.fraction ?: existingEntity?.fraction
            this.level = saveModel.level ?: existingEntity?.level
            this.magicSchool = saveModel.magicSchool ?: existingEntity?.magicSchool
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
            if (patchModel.type != null) {
                this.type = patchModel.type
            }
            if (patchModel.code != null) {
                this.code = patchModel.code
            }
            if (patchModel.name != null) {
                this.name = patchModel.name
            }
            if (patchModel.description != null) {
                this.description = patchModel.description
            }
            if (patchModel.imgOrigUrl != null) {
                this.imgOrigUrl = patchModel.imgOrigUrl
            }
            if (patchModel.cost != null) {
                this.cost = toEntityCost(patchModel.cost)
            }
            if (patchModel.fraction != null) {
                this.fraction = patchModel.fraction
            }
            if (patchModel.level != null) {
                this.level = patchModel.level
            }
            if (patchModel.magicSchool != null) {
                this.magicSchool = patchModel.magicSchool
            }
            this.auditDate = now
            existingEntity.version?.let { this.version = it }
        }

    private fun toEntityCost(elements: List<AssetDefSaveModel.CostElement>): String {
        val assetDefsByCode = assetDefDao
            .findByCodeIn(elements.map { it.assetDefCode }.toSet())
            .associateBy(AssetDef::code)
            .let { it as Map<String, AssetDef> }

        val dtoElements = elements.map {
            val assetDefId = assetDefsByCode.getValue(it.assetDefCode).id!!
            AssetDefDto.CostElement(
                assetDefId = assetDefId,
                assetDefCode = it.assetDefCode,
                count = it.count,
            )
        }

        return MAPPER.writeValueAsString(dtoElements)
    }

    companion object {

        private val MAPPER: ObjectMapper = JsonMapper.builder()
            .addModule(
                KotlinModule.Builder()
                    .configure(KotlinFeature.StrictNullChecks, true)
                    .build(),
            )
            .build()
    }
}
