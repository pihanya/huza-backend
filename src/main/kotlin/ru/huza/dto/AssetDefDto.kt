package ru.huza.dto

data class AssetDefDto(
    val id: Long? = null,
    val type: String,
    val code: String,
    val name: String,
    val description: String? = null,
    val img75Url: String? = null,
    val img130Url: String? = null,
    val img250Url: String? = null,
    val imgOrigUrl: String? = null
)
