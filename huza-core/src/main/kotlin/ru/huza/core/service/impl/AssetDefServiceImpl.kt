package ru.huza.core.service.impl

import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.model.dto.AssetDefDto
import ru.huza.core.model.dto.AssetDefPatchDto
import ru.huza.core.service.AssetDefService
import ru.huza.data.dao.AssetDefDao
import ru.huza.data.entity.Asset
import ru.huza.data.entity.AssetDef

@Service
class AssetDefServiceImpl : AssetDefService {

    @set:Autowired
    lateinit var assetDefDao: AssetDefDao

    @Transactional
    override fun save(entity: AssetDefDto): AssetDefDto =
        assetDefDao.save(toEntity(entity)).let(::toDto)

    @Transactional
    override fun patchById(id: Long, dto: AssetDefPatchDto): AssetDefDto {
        check(assetDefDao.existsById(id)) { "Asset Def with id [$id] does not exist" }

        val entity = assetDefDao.findByIdOrNull(id)
        check(entity != null) { "Asset Def with id [$id] does not exist" }

        val updatedEntity = AssetDef(entity).apply {
            this.type = dto.type ?: this.type
            this.code = dto.code ?: this.code
            this.name = dto.name ?: this.name
            this.description = dto.description ?: this.description
            this.imgOrigUrl = dto.imgOrigUrl ?: this.imgOrigUrl
            this.auditDate = LocalDateTime.now()
        }

        return assetDefDao.save(updatedEntity)
            .let(::toDto)
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

    private fun toEntity(dto: AssetDefDto): AssetDef = AssetDef().apply {
        this.id = dto.id
        this.type = dto.type
        this.code = dto.code
        this.name = dto.name
        this.description = dto.description
        this.imgOrigUrl = dto.imgOrigUrl
    }

    private fun toDto(entity: AssetDef): AssetDefDto = AssetDefDto(
        id = entity.id,
        type = entity.type!!,
        code = entity.code!!,
        name = entity.name!!,
        description = entity.description,
        imgOrigUrl = entity.imgOrigUrl
    )
}
