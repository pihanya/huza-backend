package ru.huza.core.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AssetDefSaveModel(
    val type: String? = null,
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val imgOrigUrl: String? = null,
    val cost: List<CostElement> = emptyList(),
) {

    data class CostElement(
        @JsonProperty("name") val assetDefCode: String,
        @JsonProperty("count") val count: Int,
    )
}
