package ru.huza.dao

import org.springframework.data.repository.CrudRepository
import ru.huza.entity.AssetDef

interface AssetDefDao : CrudRepository<AssetDef, Long> {

    fun findByCode(code: String): AssetDef?
}
