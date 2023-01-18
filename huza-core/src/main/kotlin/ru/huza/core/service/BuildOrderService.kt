package ru.huza.core.service

import ru.huza.core.model.dto.BuildOrderDto
import ru.huza.core.model.request.BuildOrderCreateRequest
import ru.huza.core.model.request.BuildOrderSetStatusRequest

interface BuildOrderService {

    fun createNew(request: BuildOrderCreateRequest): BuildOrderDto

    fun findAll(): List<BuildOrderDto>

    fun findAllSorted(): List<BuildOrderDto>

    fun findById(id: Long): BuildOrderDto

    fun setStatus(id: Long, request: BuildOrderSetStatusRequest): BuildOrderDto
}
