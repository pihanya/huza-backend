package ru.huza.core.model.dto

import ru.huza.data.model.enum.BuildOrderStatus

data class BuildOrderDto(
    val id: Long? = null,
    val assetDef: AssetDefDto,
    val createdAsset: AssetLink? = null,
    val status: BuildOrderStatus,
    val comment: String? = null,
    val ordinal: Long? = null
)
