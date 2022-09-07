package ru.huza.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import ru.huza.entity.Asset

@NoRepositoryBean
interface AssetDao : CrudRepository<Asset, Long> {

    fun findByFilter(filter: Map<String, Any>): List<Asset>

    object FilterParamNames {

        const val ASSET_DEF_TYPE: String = "assetDefType"

        const val ASSET_DEF_CODE: String = "assetDefCode"
    }
}
