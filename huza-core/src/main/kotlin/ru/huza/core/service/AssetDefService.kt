package ru.huza.core.service

import ru.huza.core.model.dto.AssetDefDto

interface AssetDefService {

    fun save(entity: AssetDefDto): AssetDefDto

    fun findById(id: Long): AssetDefDto

    fun findByCode(code: String): AssetDefDto?

    fun findAll(): List<AssetDefDto>
}
