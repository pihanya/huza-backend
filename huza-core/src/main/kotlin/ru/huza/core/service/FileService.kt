package ru.huza.core.service

import org.springframework.core.io.Resource
import ru.huza.core.model.dto.FileDto
import ru.huza.core.model.dto.FileSaveModel

interface FileService {

    fun create(model: FileSaveModel): FileDto

    fun createInternal(id: String, model: FileSaveModel): FileDto

    fun findById(id: String): FileDto

    fun findByIdOrNull(id: String): FileDto?

    fun getResource(dto: FileDto): Resource
}
