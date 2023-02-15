package ru.huza.core.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class AssetDefDto(
    val id: Long? = null,
    val type: String,
    val code: String,
    val name: String,
    val imgOrigUrl: String,
    val description: String? = null,
    val creationDate: LocalDateTime,
    val auditDate: LocalDateTime,
    val cost: List<CostElement>,
) {

    data class CostElement(
        @JsonProperty("id") val assetDefId: Long,
        @JsonProperty("name") val assetDefCode: String,
        @JsonProperty("count") val count: Int,
    )
}
