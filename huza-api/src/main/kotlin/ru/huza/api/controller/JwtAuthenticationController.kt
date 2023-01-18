package ru.huza.api.controller

import java.time.Instant
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.huza.api.model.request.AuthRequest
import ru.huza.api.model.response.AuthInfoResponse
import ru.huza.api.model.response.AuthResponse
import ru.huza.core.model.dto.UserDto
import ru.huza.core.service.UserService

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(path = ["/auth"])
class JwtAuthenticationController {

    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var encoder: JwtEncoder

    @Autowired
    lateinit var decoder: JwtDecoder

//    @PostMapping(path = ["/register"])
//    fun register(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
//        val (email, password) = authRequest
//        val encryptedPassword = passwordEncoder.encode(password)
//
//        if (userDao.findByEmail(email) != null) {
//            throw BadCredentialsException("User with email [$email] already exists")
//        }
//
//        userDao.save(
//            User().apply {
//                this.email = email
//                this.password = encryptedPassword
//            }
//        )
//        authenticate(email, password)
//
//        val token = jwtTokenUtil.generateToken(
//            userService.loadUserByUsername(email)
//        )
//
//        return ResponseEntity.ok(AuthResponse(token))
//    }

    @GetMapping(path = ["/info"])
    fun info(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<AuthInfoResponse> {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build()
        }

        val jwtTokenStr = authorizationHeader.removePrefix("Bearer ")
        val jwtToken: Jwt = try {
            decoder.decode(jwtTokenStr)
        } catch (ex: JwtException) {
            logger.catching(ex)
            return ResponseEntity.badRequest().build()
        }

        return ResponseEntity.ok(
            AuthInfoResponse(
                username = jwtToken.subject,
                roles = jwtToken.getClaimAsString("scope")
                    ?.split(" ")
                    .orEmpty(),
            ),
        )
    }

    @PostMapping(path = ["/login"])
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        val now: Instant = Instant.now()
        val expiry = 36000L

        val (username, email, password) = authRequest

        val effectiveUsername = checkNotNull(username ?: email)
        authenticate(effectiveUsername, password)

        val serviceUser: UserDto = userService.loadUserByUsername(effectiveUsername)
        val scope: String = serviceUser.authorities.joinToString(
            separator = " ",
            transform = GrantedAuthority::getAuthority,
        )
        val claims: JwtClaimsSet = JwtClaimsSet.builder()
            .issuer("Huza Ltd.")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiry))
            .subject(serviceUser.username)
            .claim("scope", scope)
            .build()
        val token = encoder.encode(JwtEncoderParameters.from(claims)).tokenValue

        return ResponseEntity.ok(AuthResponse(token))
    }

    private fun authenticate(username: String, password: String) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
    }
}
