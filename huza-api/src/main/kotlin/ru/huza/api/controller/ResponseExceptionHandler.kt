package ru.huza.api.controller

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.huza.api.model.response.HuzaErrorResponse
import ru.huza.core.exception.NotFoundException

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class ResponseExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleException(
        ex: NotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<HuzaErrorResponse> {
        logger.error(ex) { "Handled exception during HTTP request" }

        val response = HuzaErrorResponse(
            message = ex.message ?: ex::class.java.name,
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleException(
        ex: RuntimeException,
        request: HttpServletRequest,
    ): ResponseEntity<HuzaErrorResponse> {
        logger.error(ex) { "Handled exception during HTTP request" }

        val response = HuzaErrorResponse(
            message = ex.message ?: ex::class.java.name,
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
}
