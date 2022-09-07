package ru.huza.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.dao.AssetDao
import ru.huza.dao.AssetDefDao
import ru.huza.dto.AssetDefLink
import ru.huza.dto.AssetDto
import ru.huza.dto.toLink
import ru.huza.entity.Asset
import ru.huza.service.AssetService

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
            this.assetDef = assetDefDao.findByIdOrNull(entity.assetDef.id)
        }
        return assetDao.save(entityToSave).let(::toDto)
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
