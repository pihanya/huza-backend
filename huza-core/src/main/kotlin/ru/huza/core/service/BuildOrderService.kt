package ru.huza.core.service

import ru.huza.core.model.dto.BuildOrderDto
import ru.huza.core.model.request.BuildOrderSaveModel
import ru.huza.core.model.request.BuildOrderSetStatusRequest

interface BuildOrderService {

    fun create(request: BuildOrderSaveModel): BuildOrderDto

    fun findAll(): List<BuildOrderDto>

    fun findAllSorted(): List<BuildOrderDto>

    fun findById(id: Long): BuildOrderDto

    fun setStatus(id: Long, request: BuildOrderSetStatusRequest): BuildOrderDto

    fun increasePriority(id: Long): BuildOrderDto

    fun decreasePriority(id: Long): BuildOrderDto
}
