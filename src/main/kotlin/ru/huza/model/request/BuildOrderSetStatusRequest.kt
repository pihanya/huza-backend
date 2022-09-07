package ru.huza.model.request

import ru.huza.model.BuildOrderStatus

data class BuildOrderSetStatusRequest(

    val status: BuildOrderStatus
)
