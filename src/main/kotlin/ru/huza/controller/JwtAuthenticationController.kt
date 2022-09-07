package ru.huza.controller

import java.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.huza.config.JwtProperties
import ru.huza.dao.UserDao
import ru.huza.dto.UserDto
import ru.huza.model.request.AuthRequest
import ru.huza.model.response.AuthResponse
import ru.huza.service.UserService

@CrossOrigin
@RestController
@RequestMapping(path = ["/auth"])
class JwtAuthenticationController(
    jwtProperties: JwtProperties,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,

    private val userDao: UserDao
) {

    @set:Autowired(required = false)
    @set:Lazy
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var encoder: JwtEncoder

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

    @PostMapping(path = ["/login"])
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        val now: Instant = Instant.now()
        val expiry = 36000L

        val (email, password) = authRequest
        authenticate(email, password)

        val serviceUser: UserDto = userService.loadUserByUsername(email)
        val scope: String = serviceUser.authorities.joinToString(
            separator = " ",
            transform = GrantedAuthority::getAuthority
        )
        val claims: JwtClaimsSet = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiry))
            .subject(authRequest.email)
            .claim("scope", scope)
            .build()
        val token = encoder.encode(JwtEncoderParameters.from(claims)).tokenValue

        return ResponseEntity.ok(AuthResponse(token))
    }

    private fun authenticate(email: String, password: String) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
    }
}
