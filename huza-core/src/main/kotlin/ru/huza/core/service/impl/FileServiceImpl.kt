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
import java.nio.file.attribute.FileAttribute
import java.nio.file.attribute.PosixFilePermissions
import java.util.UUID
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeBytes
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
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

        val dataPath = dataPathOf(fileId)
        val metaPath = metaDataPathOf(fileId)
        val metadata = FileMetadata(
            fileName = model.fileName,
            mediaType = model.mediaType,
        )

        runCatching {
            dataPath.writeBytes(
                model.resource.inputStream.readBytes(),
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
            }.onFailure { ex -> logger.warn(ex) { "Could not delete data file [$dataPath] on failure" } }

            runCatching {
                if (metaPath.exists()) {
                    metaPath.toFile().delete()
                }
            }.onFailure { ex -> logger.warn(ex) { "Could not delete meta file [$metaPath] on failure" } }

            error("Could not create file: ${ex.message}")
        }

        return findById(fileId)
    }

    override fun findById(id: String): FileDto {
        val dataPath = dataPathOf(fileId = id)
        val metaPath = metaDataPathOf(fileId = id)

        val fileExists = metaPath.exists() && dataPath.exists()
        if (!fileExists) {
            throw NotFoundException("File [$id] does not exist")
        }

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
