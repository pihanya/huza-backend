package ru.huza.model.request

data class BuildOrderCreateRequest(
    val assetDefId: Long,
    val comment: String? = null,
    val pushFront: Boolean? = null
)
