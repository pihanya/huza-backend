package ru.huza.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.Serializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, AccessDeniedHandler, Serializable {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        LOGGER.debug("", authException)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
    }

    companion object {

        private val LOGGER: Logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        LOGGER.debug("", accessDeniedException)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, accessDeniedException.message)
    }
}
