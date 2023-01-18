package ru.huza.core.model.dto

data class AssetDto(
    val id: Long? = null,
    val assetDef: AssetDefLink,
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val quantity: Long
)
