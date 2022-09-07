package ru.huza.dto

import ru.huza.entity.Asset

fun Asset.toLink(): AssetLink = AssetLink(id = this.id!!)

fun AssetDto.toLink(): AssetLink = AssetLink(id = this.id!!)

data class AssetLink(
    val id: Long
)
