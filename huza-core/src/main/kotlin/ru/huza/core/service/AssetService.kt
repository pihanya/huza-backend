package ru.huza.core.service

import ru.huza.core.model.dto.AssetDto
import ru.huza.core.model.dto.AssetPatchModel
import ru.huza.core.model.dto.AssetSaveModel

interface AssetService {

    fun create(model: AssetSaveModel): AssetDto

    fun updateById(id: Long, model: AssetSaveModel): AssetDto

    fun patchById(id: Long, model: AssetPatchModel): AssetDto

    fun findById(id: Long): AssetDto

    fun findAll(): List<AssetDto>

    fun findByFilter(filter: AssetFilter): List<AssetDto>

    fun removeById(id: Long): Boolean

    data class AssetFilter(

        val code: String? = null,

        val assetDefCode: String? = null,

        val assetDefType: String? = null
    )
}
