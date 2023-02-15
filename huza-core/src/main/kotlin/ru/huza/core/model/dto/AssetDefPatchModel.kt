package ru.huza.core.model.dto

data class AssetDefPatchModel(
    val type: String? = null,
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val imgOrigUrl: String? = null,
    val cost: List<AssetDefSaveModel.CostElement>? = null,
)
