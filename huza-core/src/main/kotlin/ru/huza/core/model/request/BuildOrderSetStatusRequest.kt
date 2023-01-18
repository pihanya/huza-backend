package ru.huza.core.model.request

import ru.huza.data.model.enum.BuildOrderStatus

data class BuildOrderSetStatusRequest(

    val status: BuildOrderStatus,
)
