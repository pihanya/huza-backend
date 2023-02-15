package ru.huza.core.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import java.io.FileOutputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.PosixFilePermissions
import java.util.UUID
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeBytes
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import ru.huza.core.exception.NotFoundException
import ru.huza.core.model.dto.FileDto
import ru.huza.core.model.dto.FileSaveModel
import ru.huza.core.service.FileService

private val logger = KotlinLogging.logger {}

@Service
class FileServiceImpl : FileService {

    @Value("\${huza.file-storage-path}")
    lateinit var storagePath: Path

    @PostConstruct
    fun init() {
        storagePath.createDirectories(
            PosixFilePermissions.asFileAttribute(
                PosixFilePermissions.fromString("rwxr-x---"),
            ),
        )
    }

    override fun create(model: FileSaveModel): FileDto {
        val fileId = UUID.randomUUID().toString()
        return createInternal(id = fileId, model = model)
    }

    override fun createInternal(id: String, model: FileSaveModel) : FileDto {
        if (findByIdOrNull(id) != null) throw IllegalStateException("File with id [$id] already exists!")

        val dataPath = dataPathOf(id)
        val metaPath = metaDataPathOf(id)
        val metadata = FileMetadata(
            fileName = model.fileName,
            mediaType = model.mediaType,
        )

        storeFile(dataPath, model.resource, metaPath, metadata)

        return findById(id)
    }

    override fun findById(id: String): FileDto =
        findByIdOrNull(id)
            ?: throw NotFoundException("File [$id] does not exist")

    override fun findByIdOrNull(id: String): FileDto? {
        val dataPath = dataPathOf(fileId = id)
        val metaPath = metaDataPathOf(fileId = id)

        val fileExists = metaPath.exists() && dataPath.exists()
        if (!fileExists) return null

        val metadata = MAPPER.readValue<FileMetadata>(metaPath.toFile())

        return FileDto(
            id = id,
            fileName = metadata.fileName,
            mediaType = metadata.mediaType,
        )
    }

    override fun getResource(dto: FileDto): Resource {
        val dataPath = dataPathOf(fileId = dto.id)
        return FileSystemResource(dataPath)
    }

    private fun storeFile(
        dataPath: Path,
        dataResource: Resource,
        metaPath: Path,
        metadata: FileMetadata
    ) {
        runCatching {
            dataPath.writeBytes(
                dataResource.inputStream.readBytes(),
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE_NEW,
                StandardOpenOption.SYNC,
            )
            MAPPER.writeValue(FileOutputStream(metaPath.toFile()), metadata)
        }.onFailure { ex ->
            runCatching {
                if (dataPath.exists()) {
                    dataPath.toFile().delete()
                }
            }.onFailure { delEx -> logger.warn(delEx) { "Could not delete data file [$dataPath] on failure" } }

            runCatching {
                if (metaPath.exists()) {
                    metaPath.toFile().delete()
                }
            }.onFailure { delEx -> logger.warn(delEx) { "Could not delete meta file [$metaPath] on failure" } }

            error("Could not create file: ${ex.message}")
        }
    }

    private fun dataPathOf(fileId: String): Path =
        storagePath.resolve("$fileId.dat")

    private fun metaDataPathOf(fileId: String): Path =
        storagePath.resolve("$fileId.json")

    companion object {

        private val MAPPER: ObjectMapper = JsonMapper.builder()
            .addModule(
                KotlinModule.Builder()
                    .configure(KotlinFeature.StrictNullChecks, true)
                    .build()
            )
            .build()
    }

    private data class FileMetadata(
        val fileName: String,
        val mediaType: String,
    )
}
