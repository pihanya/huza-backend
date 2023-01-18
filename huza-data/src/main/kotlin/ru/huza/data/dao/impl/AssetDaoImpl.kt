package ru.huza.data.dao.impl

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.stereotype.Repository
import ru.huza.data.dao.AssetDao
import ru.huza.data.entity.Asset
import ru.huza.data.entity.AssetDef

@Repository
class AssetDaoImpl @Autowired constructor(
    private val entityManager: EntityManager,
) : AssetDao, SimpleJpaRepository<Asset, Long>(Asset::class.java, entityManager) {

    override fun findByFilter(filter: Map<String, Any>): List<Asset> {
        val cb = entityManager.criteriaBuilder
        val cq = cb.createQuery(Asset::class.java)

        val root = cq.from(Asset::class.java)
        cq.select(root)

        val predicates = buildList {
            if (Asset.CODE in filter) {
                this += cb.equal(
                    root.get<Asset>(Asset.CODE),
                    cb.parameter(String::class.java, Asset.CODE),
                )
            }
            if (AssetDao.FilterParamNames.ASSET_DEF_CODE in filter) {
                this += cb.equal(
                    root.get<Asset>(Asset.ASSET_DEF).get<AssetDef>(AssetDef.CODE),
                    cb.parameter(String::class.java, AssetDao.FilterParamNames.ASSET_DEF_CODE),
                )
            }
            if (AssetDao.FilterParamNames.ASSET_DEF_TYPE in filter) {
                this += cb.equal(
                    root.get<Asset>(Asset.ASSET_DEF).get<AssetDef>(AssetDef.CODE),
                    cb.parameter(String::class.java, AssetDao.FilterParamNames.ASSET_DEF_TYPE),
                )
            }
        }
        cq.where(cb.and(*predicates.toTypedArray()))

        val query = entityManager.createQuery(cq)
        filter.forEach { (key, value) -> query.setParameter(key, value) }

        return query.resultList
    }
}
