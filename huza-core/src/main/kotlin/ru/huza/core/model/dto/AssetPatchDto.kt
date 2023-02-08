package ru.huza.core.model.dto

data class AssetPatchDto(
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val quantity: Long? = null,
)
