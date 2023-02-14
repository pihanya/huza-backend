package ru.huza.core.model.dto

data class AssetSaveModel(
    val assetDefId: Long? = null,
    val quantity: Long? = null,
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
)
