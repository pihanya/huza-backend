package ru.huza.core.service

import ru.huza.core.model.dto.AssetDefDto
import ru.huza.core.model.dto.AssetDefPatchDto

interface AssetDefService {

    fun save(entity: AssetDefDto): AssetDefDto

    fun patchById(id: Long, dto: AssetDefPatchDto): AssetDefDto

    fun findById(id: Long): AssetDefDto

    fun findByCode(code: String): AssetDefDto?

    fun findAll(): List<AssetDefDto>
}
