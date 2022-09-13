package ru.huza.model.request

import java.time.LocalDateTime

data class CreateAuditReportRequest(

    val startDate: LocalDateTime? = null,

    val endDate: LocalDateTime? = null,

    val assetDefTypes: List<String>? = null
)
