package ru.huza.core.model.dto

import ru.huza.data.entity.Asset

fun Asset.toLink(): AssetLink = AssetLink(id = this.id!!)

fun AssetDto.toLink(): AssetLink = AssetLink(id = this.id!!)

data class AssetLink(
    val id: Long
)
