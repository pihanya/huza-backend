package ru.huza.core.model.dto

import org.springframework.core.io.Resource

data class FileSaveModel(
    val resource: Resource,
    val fileName: String,
    val mediaType: String,
)
