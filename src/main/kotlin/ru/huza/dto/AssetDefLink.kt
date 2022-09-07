package ru.huza.dto

import ru.huza.entity.AssetDef

fun AssetDef.toLink(): AssetDefLink = AssetDefLink(
    id = this.id!!,
    type = this.type!!,
    code = this.code!!
)

fun AssetDefDto.toLink(): AssetDefLink = AssetDefLink(
    id = this.id!!,
    type = this.type,
    code = this.code
)

data class AssetDefLink(
    val id: Long,
    val type: String,
    val code: String
)
