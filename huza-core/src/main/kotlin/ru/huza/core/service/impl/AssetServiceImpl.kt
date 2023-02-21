package ru.huza.core.service.impl

import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.exception.NotFoundException
import ru.huza.core.model.dto.AssetDto
import ru.huza.core.model.dto.AssetPatchModel
import ru.huza.core.model.dto.AssetSaveModel
import ru.huza.core.util.toDto
import ru.huza.core.service.AssetService
import ru.huza.data.dao.AssetDao
import ru.huza.data.dao.AssetDefDao
import ru.huza.data.entity.Asset

@Service
class AssetServiceImpl : AssetService {

    @set:Autowired
    lateinit var assetDao: AssetDao

    @set:Autowired
    lateinit var assetDefDao: AssetDefDao

    @Transactional
    override fun create(model: AssetSaveModel): AssetDto {
        val now = LocalDateTime.now()

        val entityToSave = fillFromSaveModel(existingEntity = null, saveModel = model, now = now)
        return assetDao.save(entityToSave).let(::toDto)
    }

    @Transactional
    override fun updateById(id: Long, model: AssetSaveModel): AssetDto {
        val now = LocalDateTime.now()

        val existingEntity = assetDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset with id [$id] does not exist" }

        val updatedEntity = fillFromSaveModel(existingEntity = existingEntity, saveModel = model, now = now)
        return assetDao.save(updatedEntity).let(::toDto)
    }

    @Transactional
    override fun patchById(id: Long, model: AssetPatchModel): AssetDto {
        val now = LocalDateTime.now()

        val existingEntity = assetDao.findByIdOrNull(id)
        check(existingEntity != null) { "Asset with id [$id] does not exist" }

        val updatedEntity = fillFromPatchModel(existingEntity = existingEntity, patchModel = model, now = now)
        return assetDao.save(updatedEntity).let(::toDto)
    }

    override fun findById(id: Long): AssetDto =
        assetDao.findById(id).orElseThrow().let(::toDto)

    override fun findAll(): List<AssetDto> =
        assetDao.findAll().asSequence()
            .map(::toDto)
            .sortedWith(
                compareBy(
                    { it.assetDef.id },
                    AssetDto::quantity,
                ),
            )
            .toList()

    override fun findByFilter(filter: AssetService.AssetFilter): List<AssetDto> =
        assetDao.findByFilter(
            buildMap {
                if (filter.code != null) put(Asset.CODE, filter.code)
                if (filter.assetDefType != null) put(AssetDao.FilterParamNames.ASSET_DEF_TYPE, filter.assetDefType)
                if (filter.assetDefCode != null) put(AssetDao.FilterParamNames.ASSET_DEF_CODE, filter.assetDefCode)
            },
        ).map(::toDto)

    override fun removeById(id: Long): Boolean {
        assetDao.findByIdOrNull(id) ?: return false
        assetDao.deleteById(id)
        return true
    }

    private fun toDto(entity: Asset): AssetDto = AssetDto(
        id = entity.id,
        assetDef = entity.assetDef!!.toDto(),
        code = entity.code,
        name = entity.name,
        description = entity.description,
        quantity = entity.quantity!!,
    )

    private fun fillFromSaveModel(
        existingEntity: Asset? = null,
        saveModel: AssetSaveModel,
        now: LocalDateTime,
    ): Asset {
        val assetDefId = checkNotNull(saveModel.assetDefId ?: existingEntity?.assetDef?.id)
        val assetDef = assetDefDao.findByIdOrNull(assetDefId)
            ?: throw NotFoundException("Asset Def with id [$assetDefId] does not exist")

        return Asset().apply {
            this.id = existingEntity?.id
            this.assetDef = assetDef
            this.code = saveModel.code ?: existingEntity?.code
            this.name = saveModel.name ?: existingEntity?.name
            this.description = saveModel.description ?: existingEntity?.description
            this.quantity = saveModel.quantity ?: existingEntity?.quantity
            this.creationDate = existingEntity?.creationDate ?: now
            this.auditDate = now
            existingEntity?.version?.let { this.version = it }
        }
    }

    private fun fillFromPatchModel(
        existingEntity: Asset,
        patchModel: AssetPatchModel,
        now: LocalDateTime,
    ): Asset =
        Asset(existingEntity).apply {
            this.code = patchModel.code ?: existingEntity.code
            this.name = patchModel.name ?: existingEntity.name
            this.description = patchModel.description ?: existingEntity.description
            this.quantity = patchModel.quantity ?: existingEntity.quantity
            this.auditDate = now
            existingEntity.version?.let { this.version = it }
        }
}
