package ru.huza.core.model.dto

import java.time.LocalDateTime

data class AssetDefDto(
    val id: Long? = null,
    val type: String,
    val code: String,
    val name: String,
    val description: String? = null,
    val imgOrigUrl: String? = null,
    val creationDate: LocalDateTime,
    val auditDate: LocalDateTime,
)
