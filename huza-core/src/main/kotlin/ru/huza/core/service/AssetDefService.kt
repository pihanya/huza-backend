package ru.huza.core.service

import ru.huza.core.model.dto.AssetDefDto
import ru.huza.core.model.dto.AssetDefPatchModel
import ru.huza.core.model.dto.AssetDefSaveModel

interface AssetDefService {

    fun create(model: AssetDefSaveModel): AssetDefDto

    fun updateById(id: Long, model: AssetDefSaveModel): AssetDefDto

    fun patchById(id: Long, dto: AssetDefPatchModel): AssetDefDto

    fun findById(id: Long): AssetDefDto

    fun findByCode(code: String): AssetDefDto?

    fun findAll(): List<AssetDefDto>
}
