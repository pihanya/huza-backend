package ru.huza.core.model.dto

import ru.huza.data.entity.AssetDef

fun AssetDef.toLink(): AssetDefLink = AssetDefLink(
    id = this.id!!,
    type = this.type!!,
    code = this.code!!,
    name = this.name!!,
    imgOrigUrl = this.imgOrigUrl!!,
)

fun AssetDefDto.toLink(): AssetDefLink = AssetDefLink(
    id = this.id!!,
    type = this.type,
    code = this.code,
    name = this.name,
    imgOrigUrl = this.imgOrigUrl,
)

data class AssetDefLink(
    val id: Long,
    val type: String,
    val code: String,
    val name: String,
    val imgOrigUrl: String,
)