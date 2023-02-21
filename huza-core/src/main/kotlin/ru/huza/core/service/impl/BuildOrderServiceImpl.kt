package ru.huza.core.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.model.dto.BuildOrderDto
import ru.huza.core.model.dto.toLink
import ru.huza.core.model.request.BuildOrderCreateRequest
import ru.huza.core.model.request.BuildOrderSetStatusRequest
import ru.huza.core.service.BuildOrderService
import ru.huza.core.util.toDto
import ru.huza.data.dao.AssetDao
import ru.huza.data.dao.AssetDefDao
import ru.huza.data.dao.BuildOrderDao
import ru.huza.data.entity.BuildOrder
import ru.huza.data.model.enum.BuildOrderStatus

@Service
class BuildOrderServiceImpl : BuildOrderService {

    @set:Autowired
    lateinit var buildOrderDao: BuildOrderDao

    @set:Autowired
    lateinit var assetDefDao: AssetDefDao

    @set:Autowired
    lateinit var assetDao: AssetDao

    @Transactional
    override fun createNew(request: BuildOrderCreateRequest): BuildOrderDto {
        val createdEntity = buildOrderDao.save(
            BuildOrder().apply {
                this.id = null
                this.assetDef = assetDefDao.findById(request.assetDefId).orElseThrow()
                this.createdAsset = null
                this.status = BuildOrderStatus.CREATED
                this.comment = request.comment
                this.ordinal = if (request.pushFront == true) {
                    computeMinOrdinal()?.minus(1) ?: MIN_ORDINAL
                } else {
                    computeMaxOrdinal()?.plus(1) ?: MIN_ORDINAL
                }
            }
        )
        adjustOrdinals(buildOrderDao.findAll())
        return toDto(createdEntity)
    }

    @Transactional
    override fun findAll(): List<BuildOrderDto> {
        val entityList = buildOrderDao.findAll().toList()
        adjustOrdinals(entityList)
        return entityList.map(::toDto)
    }

    @Transactional
    override fun findAllSorted(): List<BuildOrderDto> {
        val entityList = buildOrderDao.findAll()
        adjustOrdinals(entityList)
        return entityList
            .map(::toDto)
            .sortedWith(
                compareBy(
                    { it.ordinal ?: Long.MAX_VALUE },
                    { it.id }
                )
            )
    }

    override fun findById(id: Long): BuildOrderDto =
        buildOrderDao.findById(id).map(::toDto).orElseThrow()

    @Transactional
    override fun setStatus(id: Long, request: BuildOrderSetStatusRequest): BuildOrderDto {
        val entity = buildOrderDao.findById(id).orElseThrow()
        val updatedEntity = buildOrderDao.save(
            entity.apply {
                this.status = request.status
                if (isTerminalStatus(request.status)) {
                    this.ordinal = null
                }
            }
        )
        return toDto(updatedEntity)
    }

    private fun computeMaxOrdinal(): Long? = findAllSorted()
        .filter { !isTerminalStatus(it.status) }
        .filter { it.ordinal != null }
        .maxOfOrNull { it.ordinal!! }

    private fun computeMinOrdinal(): Long? = findAllSorted()
        .filter { !isTerminalStatus(it.status) }
        .filter { it.ordinal != null }
        .minOfOrNull { it.ordinal!! }

    private fun isTerminalStatus(value: BuildOrderStatus): Boolean = when (value) {
        BuildOrderStatus.FINISHED -> true
        BuildOrderStatus.REFUSED -> true
        else -> false
    }

    private fun adjustOrdinals(entityList: Iterable<BuildOrder>) {
        val (withOrdinal, woOrdinal) = entityList.partition { it.ordinal != null }
        val withOrdinalSorted = withOrdinal.sortedBy { it.ordinal!! }

        var currentOrdinal = MIN_ORDINAL
        val updatedValues = buildList {
            for (value in withOrdinalSorted) {
                if (isTerminalStatus(value.status!!)) {
                    value.ordinal = null
                    add(value)
                    continue
                }
                if (value.ordinal != currentOrdinal) {
                    value.ordinal = currentOrdinal
                    add(value)
                }
                currentOrdinal++
            }

            for (value in woOrdinal) {
                if (isTerminalStatus(value.status!!)) continue
                value.ordinal = currentOrdinal++
                add(value)
            }
        }

        if (updatedValues.isNotEmpty()) {
            buildOrderDao.saveAll(updatedValues)
        }
    }

    private fun toDto(entity: BuildOrder): BuildOrderDto =
        BuildOrderDto(
            id = entity.id,
            assetDef = entity.assetDef!!.toDto(),
            createdAsset = entity.createdAsset?.toLink(),
            status = entity.status!!,
            comment = entity.comment,
            ordinal = entity.ordinal
        )

    private companion object {

        private const val MIN_ORDINAL: Long = 1
    }
}
