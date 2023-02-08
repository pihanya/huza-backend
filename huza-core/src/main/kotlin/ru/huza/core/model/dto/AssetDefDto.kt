package ru.huza.core.model.dto

data class AssetDefDto(
    val id: Long? = null,
    val type: String,
    val code: String,
    val name: String,
    val description: String? = null,
    val imgOrigUrl: String? = null
)
