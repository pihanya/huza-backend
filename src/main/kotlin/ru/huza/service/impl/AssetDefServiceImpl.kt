package ru.huza.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.dao.AssetDefDao
import ru.huza.dto.AssetDefDto
import ru.huza.entity.AssetDef
import ru.huza.service.AssetDefService

@Service
class AssetDefServiceImpl : AssetDefService {

    @set:Autowired
    lateinit var assetDefDao: AssetDefDao

    @Transactional
    override fun save(entity: AssetDefDto): AssetDefDto =
        assetDefDao.save(toEntity(entity)).let(::toDto)

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
        this.img75Url = dto.img75Url
        this.img130Url = dto.img130Url
        this.img250Url = dto.img250Url
        this.imgOrigUrl = dto.imgOrigUrl
    }

    private fun toDto(entity: AssetDef): AssetDefDto = AssetDefDto(
        id = entity.id,
        type = entity.type!!,
        code = entity.code!!,
        name = entity.name!!,
        description = entity.description,
        img75Url = entity.img75Url,
        img130Url = entity.img130Url,
        img250Url = entity.img250Url,
        imgOrigUrl = entity.imgOrigUrl
    )
}
