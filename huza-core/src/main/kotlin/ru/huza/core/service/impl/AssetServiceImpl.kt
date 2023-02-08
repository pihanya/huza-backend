package ru.huza.core.service.impl

import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.model.dto.AssetDto
import ru.huza.core.model.dto.AssetPatchDto
import ru.huza.core.model.dto.toLink
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
    override fun save(entity: AssetDto): AssetDto {
        val entityToSave = Asset().apply {
            this.id = entity.id
            this.code = entity.code
            this.name = entity.name
            this.description = entity.description
            this.quantity = entity.quantity
            this.assetDef = assetDefDao.findById(entity.assetDef.id).orElseThrow()
        }
        return assetDao.save(entityToSave).let(::toDto)
    }

    @Transactional
    override fun patchById(id: Long, dto: AssetPatchDto): AssetDto {
        check(assetDao.existsById(id)) { "Asset with id [$id] does not exist" }

        val entity = assetDao.findByIdOrNull(id)
        check(entity != null) { "Asset with id [$id] does not exist" }

        val updatedEntity = Asset(entity).apply {
            this.code = dto.code ?: this.code
            this.name = dto.name ?: this.name
            this.description = dto.description ?: this.description
            this.quantity = dto.quantity ?: this.quantity
            this.auditDate = LocalDateTime.now()
        }

        return assetDao.save(updatedEntity)
            .let(::toDto)
    }

    override fun findById(id: Long): AssetDto =
        assetDao.findById(id).orElseThrow().let(::toDto)

    override fun findAll(): List<AssetDto> =
        assetDao.findAll().asSequence()
            .map(::toDto)
            .sortedWith(
                compareBy(
                    { it.assetDef.code },
                    AssetDto::quantity
                )
            )
            .toList()

    override fun findByFilter(filter: AssetService.AssetFilter): List<AssetDto> =
        assetDao.findByFilter(
            buildMap {
                if (filter.code != null) put(Asset.CODE, filter.code)
                if (filter.assetDefType != null) put(AssetDao.FilterParamNames.ASSET_DEF_TYPE, filter.assetDefType)
                if (filter.assetDefCode != null) put(AssetDao.FilterParamNames.ASSET_DEF_CODE, filter.assetDefCode)
            }
        ).map(::toDto)

    private fun toDto(entity: Asset): AssetDto = AssetDto(
        id = entity.id,
        assetDef = entity.assetDef!!.toLink(),
        code = entity.code,
        name = entity.name,
        description = entity.description,
        quantity = entity.quantity!!
    )
}
