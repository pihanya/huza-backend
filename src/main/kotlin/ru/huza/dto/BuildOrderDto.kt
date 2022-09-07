package ru.huza.dto

import ru.huza.model.BuildOrderStatus

data class BuildOrderDto(
    val id: Long? = null,
    val assetDef: AssetDefLink,
    val createdAsset: AssetLink? = null,
    val status: BuildOrderStatus,
    val comment: String? = null,
    val ordinal: Long? = null
)
