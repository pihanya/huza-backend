package ru.huza.core.model.request

data class BuildOrderSaveModel(
    val assetDefId: Long,
    val comment: String? = null,
    val pushFront: Boolean? = null,
)
