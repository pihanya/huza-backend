package ru.huza.core.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.huza.core.exception.NotFoundException
import ru.huza.core.model.dto.AssetDefDto.CostElement
import ru.huza.core.model.dto.AssetPatchModel
import ru.huza.core.model.dto.BuildOrderDto
import ru.huza.core.model.dto.toLink
import ru.huza.core.model.request.BuildOrderCreateRequest
import ru.huza.core.model.request.BuildOrderSetStatusRequest
import ru.huza.core.service.AssetDefService
import ru.huza.core.service.AssetService
import ru.huza.core.service.AssetService.AssetFilter
import ru.huza.core.service.BuildOrderService
import ru.huza.core.util.toDto
import ru.huza.data.dao.AssetDao
import ru.huza.data.dao.AssetDefDao
import ru.huza.data.dao.BuildOrderDao
import ru.huza.data.entity.BuildOrder
import ru.huza.data.model.enum.BuildOrderStatus

@Service
class BuildOrderServiceImpl @Autowired constructor(
    private val assetDefService: AssetDefService,
    private val assetService: AssetService,
    private val buildOrderDao: BuildOrderDao,
    private val assetDefDao: AssetDefDao,
) : BuildOrderService {

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
            },
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
                    BuildOrderDto::id,
                ),
            )
    }

    override fun findById(id: Long): BuildOrderDto =
        buildOrderDao.findById(id).map(::toDto).orElseThrow()

    @Transactional
    override fun setStatus(id: Long, request: BuildOrderSetStatusRequest): BuildOrderDto {
        val entity = buildOrderDao.findByIdOrNull(id)
            ?: throw NotFoundException("Build Order with id [$id] does not exist")

        checkTransitionPossible(entity, toStatus = request.status)

        // Withdraw cost if transition into "IN_PROGRESS"
        if (entity.status == BuildOrderStatus.CREATED && request.status == BuildOrderStatus.IN_PROGRESS) {
            withdrawCost(entity)
        }

        entity.apply {
            this.status = request.status
            if (isTerminalStatus(request.status)) {
                this.ordinal = null
            }
        }

        val updatedEntity = buildOrderDao.save(entity)
        return toDto(updatedEntity)
    }

    @Transactional
    override fun increasePriority(id: Long): BuildOrderDto {
        val allOrders = findAllSorted()

        data class OrderWithNewOrdinal(val entity: BuildOrderDto, val newOrdinal: Long)

        var currentCtr = MIN_ORDINAL
        val newOrdinals = mutableListOf<OrderWithNewOrdinal>()
        for (order in allOrders) {
            newOrdinals += OrderWithNewOrdinal(order, newOrdinal = currentCtr++)
        }

        val idx = newOrdinals.indexOfFirst { (order, _) -> order.id == id }
        check(idx != -1) { "Couldn't find order with id [$id]" }
        if (idx == 0) return newOrdinals[idx].entity // Already max priority

        val (currentOrdinal, increasedOrdinal) = newOrdinals[idx].newOrdinal to newOrdinals[idx - 1].newOrdinal
        newOrdinals[idx] = newOrdinals[idx].copy(newOrdinal = increasedOrdinal)
        newOrdinals[idx - 1] = newOrdinals[idx - 1].copy(newOrdinal = currentOrdinal)

        for ((entity, newOrdinal) in newOrdinals) {
            setOrdinal(id = entity.id!!, ordinal = newOrdinal)
        }

        return findById(id)
    }

    @Transactional
    override fun decreasePriority(id: Long): BuildOrderDto {
        val allOrders = findAllSorted()

        data class OrderWithNewOrdinal(val entity: BuildOrderDto, val newOrdinal: Long)

        var currentCtr = MIN_ORDINAL
        val newOrdinals = mutableListOf<OrderWithNewOrdinal>()
        for (order in allOrders) {
            newOrdinals += OrderWithNewOrdinal(order, newOrdinal = currentCtr++)
        }

        val idx = newOrdinals.indexOfFirst { (order, _) -> order.id == id }
        check(idx != -1) { "Couldn't find order with id [$id]" }
        if (idx == newOrdinals.size - 1) return newOrdinals[idx].entity // Already min priority

        val (currentOrdinal, decreasedOrdinal) = newOrdinals[idx].newOrdinal to newOrdinals[idx + 1].newOrdinal
        newOrdinals[idx] = newOrdinals[idx].copy(newOrdinal = decreasedOrdinal)
        newOrdinals[idx + 1] = newOrdinals[idx + 1].copy(newOrdinal = currentOrdinal)

        for ((entity, newOrdinal) in newOrdinals) {
            setOrdinal(id = entity.id!!, ordinal = newOrdinal)
        }

        return findById(id)
    }

    private fun setOrdinal(id: Long, ordinal: Long) {
        val entity = checkNotNull(buildOrderDao.findByIdOrNull(id))
        entity.ordinal = ordinal
        buildOrderDao.save(entity)
    }

    private fun checkTransitionPossible(
        entity: BuildOrder,
        toStatus: BuildOrderStatus,
    ) {
        val fromStatus = checkNotNull(entity.status)
        val assetDefId = checkNotNull(entity.assetDef?.id)

        val allowedTransitions = when (fromStatus) {
            BuildOrderStatus.CREATED -> listOf(
                BuildOrderStatus.CREATED,
                BuildOrderStatus.REFUSED,
                BuildOrderStatus.IN_PROGRESS,
            )

            BuildOrderStatus.IN_PROGRESS -> listOf(
                BuildOrderStatus.IN_PROGRESS,
                BuildOrderStatus.REFUSED,
                BuildOrderStatus.FINISHED,
            )

            BuildOrderStatus.FINISHED -> listOf(BuildOrderStatus.FINISHED)
            BuildOrderStatus.REFUSED -> listOf(BuildOrderStatus.REFUSED)
        }

        if (toStatus !in allowedTransitions) {
            throw IllegalStateException(
                "Transition from status [$fromStatus] to [$toStatus]" +
                    " is not possible for build order [${entity.id}]",
            )
        }

        // Check there are enough resources to build this order
        if (fromStatus == BuildOrderStatus.CREATED && toStatus == BuildOrderStatus.IN_PROGRESS) {
            val assetDef = assetDefService.findById(assetDefId)
            val costs = assetDef.cost

            if (costs.isNotEmpty()) {
                val costAssets = costs
                    .map(CostElement::assetDefCode)
                    .flatMap { code -> assetService.findByFilter(AssetFilter(assetDefCode = code)) }

                val costByAssetDefCode = costs.associateBy(CostElement::assetDefCode)
                val costAssetByAssetDefCode = costAssets.associateBy { ass -> ass.assetDef.code }

                val missingAmountByResAssetDefCode = mutableMapOf<String, Int>()
                for ((assetDefCode, cost: CostElement) in costByAssetDefCode) {
                    val costAsset = costAssetByAssetDefCode[assetDefCode]
                    val costAssetQuantity: Int = costAsset?.quantity?.toInt() ?: 0
                    if (costAssetQuantity < cost.count) {
                        missingAmountByResAssetDefCode[assetDefCode] = cost.count - costAssetQuantity
                    }
                }

                if (missingAmountByResAssetDefCode.isNotEmpty()) {
                    throw RuntimeException(
                        buildString {
                            append("Недостаточно ресурсов для постройки [${assetDef.name}]:\n")
                            missingAmountByResAssetDefCode.asSequence()
                                .forEach { (resCode, missAmount) ->
                                    append(missAmount, " ", resCode)
                                    appendLine()
                                }
                        },
                    )
                }
            }
        }
    }

    fun withdrawCost(entity: BuildOrder) {
        val assetDefId = checkNotNull(entity.assetDef?.id)
        val assetDef = assetDefService.findById(assetDefId)

        val costs = assetDef.cost
        if (costs.isEmpty()) return

        val costAssets = costs.map(CostElement::assetDefCode)
            .flatMap { code -> assetService.findByFilter(AssetFilter(assetDefCode = code)) }

        val costByAssetDefCode = costs.associateBy(CostElement::assetDefCode)
        val costAssetByAssetDefCode = costAssets.associateBy { ass -> ass.assetDef.code }

        for ((assetDefCode, cost: CostElement) in costByAssetDefCode) {
            val costAsset = costAssetByAssetDefCode.getValue(assetDefCode)
            val newQuantity = costAsset.quantity - cost.count.toLong()
            assetService.patchById(id = costAsset.id!!, model = AssetPatchModel(quantity = newQuantity))
        }
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
            ordinal = entity.ordinal,
        )

    private companion object {

        private const val MIN_ORDINAL: Long = 1
    }
}
