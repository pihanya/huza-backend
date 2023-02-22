package ru.huza.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.huza.api.HttpEndpoints
import ru.huza.core.model.dto.FileDto
import ru.huza.core.model.dto.FileSaveModel
import ru.huza.core.service.FileService

@RestController
@RequestMapping(path = [HttpEndpoints.FILES])
class FileController {

    @set:Autowired
    lateinit var fileService: FileService

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createFile(
        @RequestParam("file") file: MultipartFile,
    ): FileDto =
        fileService.create(
            FileSaveModel(
                resource = file.resource,
                fileName = file.originalFilename ?: file.name,
                mediaType = file.contentType ?: MediaType.ALL_VALUE
            )
        )

    @GetMapping("/download/{id}")
    fun downloadFile(
        @PathVariable id: String
    ): ResponseEntity<Resource> {
        val existingFile = fileService.findById(id)
        val fileResource = fileService.getResource(existingFile)

        val responseHeaders = HttpHeaders().apply {
            contentType = MediaType.parseMediaType(existingFile.mediaType)
            contentLength = fileResource.contentLength()
            contentDisposition = ContentDisposition
                .attachment()
                .filename(existingFile.fileName)
                .build()
        }

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(fileResource)
    }
}
