package ru.huza.api.model.request

data class AssetSaveModel(
    val assetDefId: Long?,
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val quantity: Long? = null,
)
