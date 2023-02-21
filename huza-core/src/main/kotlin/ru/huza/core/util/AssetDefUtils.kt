package ru.huza.core.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import ru.huza.core.model.dto.AssetDefDto
import ru.huza.data.entity.AssetDef

private val MAPPER: ObjectMapper = JsonMapper.builder()
    .addModule(
        KotlinModule.Builder()
            .configure(KotlinFeature.StrictNullChecks, true)
            .build(),
    )
    .build()

fun AssetDef.toDto(): AssetDefDto =
    AssetDefDto(
        id = this.id,
        type = this.type!!,
        code = this.code!!,
        name = this.name!!,
        imgOrigUrl = this.imgOrigUrl!!,
        description = this.description,
        cost = this.cost?.let { MAPPER.readValue<List<AssetDefDto.CostElement>>(it) }.orEmpty(),
        fraction = this.fraction,
        level = this.level,
        magicSchool = this.magicSchool,
        creationDate = this.creationDate ?: error("creationDate was null for this [${this.id}]"),
        auditDate = this.auditDate ?: error("auditDate was null for this [${this.id}]"),
    )
