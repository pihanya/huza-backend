package ru.huza.core.model.dto

import java.time.LocalDateTime

data class AssetAuditRecord(
    val id: Long,
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val quantity: Long,
    val creationDate: LocalDateTime,
    val auditDate: LocalDateTime,
    val revision: Long,
    val assetDefId: Long,
    val assetDefCode: String,
    val assetDefName: String,
    val assetDefType: String
)
