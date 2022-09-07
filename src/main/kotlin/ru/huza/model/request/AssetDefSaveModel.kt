package ru.huza.model.request

data class AssetDefSaveModel(
    val type: String? = null,
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val img75Url: String? = null,
    val img130Url: String? = null,
    val img250Url: String? = null,
    val imgOrigUrl: String? = null
)
