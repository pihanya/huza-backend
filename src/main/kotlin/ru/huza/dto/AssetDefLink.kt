package ru.huza.dto

import ru.huza.entity.AssetDef

fun AssetDef.toLink(): AssetDefLink = AssetDefLink(
    id = this.id!!,
    type = this.type!!,
    code = this.code!!,
    name = this.name!!
)

fun AssetDefDto.toLink(): AssetDefLink = AssetDefLink(
    id = this.id!!,
    type = this.type,
    code = this.code,
    name = this.name
)

data class AssetDefLink(
    val id: Long,
    val type: String,
    val code: String,
    val name: String
)
