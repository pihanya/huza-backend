package ru.huza.data.dao

import org.springframework.data.repository.CrudRepository
import ru.huza.data.entity.AssetDef

interface AssetDefDao : CrudRepository<AssetDef, Long> {

    fun findByCode(code: String): AssetDef?

    fun findByCodeIn(codes: Set<String>): List<AssetDef>
}
