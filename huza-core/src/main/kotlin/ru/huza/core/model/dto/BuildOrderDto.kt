package ru.huza.core.model.dto

import ru.huza.core.model.dto.AssetDefLink
import ru.huza.core.model.dto.AssetLink
import ru.huza.data.model.enum.BuildOrderStatus

data class BuildOrderDto(
    val id: Long? = null,
    val assetDef: AssetDefLink,
    val createdAsset: AssetLink? = null,
    val status: BuildOrderStatus,
    val comment: String? = null,
    val ordinal: Long? = null
)
