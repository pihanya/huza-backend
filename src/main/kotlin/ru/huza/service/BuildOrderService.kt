package ru.huza.service

import ru.huza.dto.BuildOrderDto
import ru.huza.model.request.BuildOrderCreateRequest
import ru.huza.model.request.BuildOrderSetStatusRequest

interface BuildOrderService {

    fun createNew(request: BuildOrderCreateRequest): BuildOrderDto

    fun findAll(): List<BuildOrderDto>

    fun findAllSorted(): List<BuildOrderDto>

    fun findById(id: Long): BuildOrderDto

    fun setStatus(id: Long, request: BuildOrderSetStatusRequest): BuildOrderDto
}
