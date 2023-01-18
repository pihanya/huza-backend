package ru.huza.core.service

import ru.huza.core.model.dto.AssetDto

interface AssetService {

    fun save(entity: AssetDto): AssetDto

    fun findById(id: Long): AssetDto

    fun findAll(): List<AssetDto>

    fun findByFilter(filter: AssetFilter): List<AssetDto>

    data class AssetFilter(

        val code: String? = null,

        val assetDefCode: String? = null,

        val assetDefType: String? = null
    )
}
