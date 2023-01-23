package ru.huza.service

import ru.huza.dto.AssetDefDto

interface AssetDefService {

    fun save(entity: AssetDefDto): AssetDefDto

    fun findById(id: Long): AssetDefDto

    fun findByCode(code: String): AssetDefDto?

    fun findAll(): List<AssetDefDto>
}