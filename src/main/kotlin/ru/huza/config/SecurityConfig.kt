package ru.huza.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import java.util.Optional
import kotlin.reflect.full.cast
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import ru.huza.security.JwtAuthenticationEntryPoint
import ru.huza.service.UserService

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig {

    @Autowired
    internal lateinit var jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint

    @Autowired
    internal lateinit var userService: UserService

    @Autowired
    internal lateinit var jwtProperties: JwtProperties

    /*@Value("\${jwt.public.key}")
    lateinit var key: RSAPublicKey

    @Value("\${jwt.private.key}")
    lateinit var priv: RSAPrivateKey*/

    @Bean
    fun authenticationProvider(): AuthenticationProvider = DaoAuthenticationProvider().apply {
        setPasswordEncoder(passwordEncoder())
        setUserDetailsService(userService)
        setUserDetailsPasswordService(userService)
    }

    @Bean
    fun authenticationManager(): AuthenticationManager =
        ProviderManager(listOf(authenticationProvider()))

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            // .csrf { csrf -> csrf.ignoringAntMatchers("/auth/token") }
            // .cors { cors -> cors.configurationSource { src -> src. } }
            .cors { cors -> cors.configurationSource { CorsConfiguration().applyPermitDefaultValues() } }
            .authorizeHttpRequests { authorize ->
                authorize
                    // .antMatchers("/").permitAll()
                    .antMatchers(HttpMethod.OPTIONS, "**").permitAll()
                    // .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                    .antMatchers("/auth/login").permitAll()
                    // .antMatchers("/actuator/**").permitAll()
                    // .antMatchers("/asset-defs/**").permitAll()
                    // .antMatchers("/build-orders/**").permitAll()
                    // .antMatchers("/assets/**").permitAll()
                    // .antMatchers("/users/**").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer<*>::jwt)
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAuthenticationEntryPoint)
            }

        return http.build()
    }

    @Bean fun jwtDecoder(): JwtDecoder =
        NimbusJwtDecoder.withPublicKey(jwtProperties.publicKey).build()

    @Bean fun jwtEncoder(): JwtEncoder =
        NimbusJwtEncoder(
            ImmutableJWKSet(
                JWKSet(
                    RSAKey.Builder(jwtProperties.publicKey)
                        .privateKey(jwtProperties.privateKey)
                        .build()
                )
            )
        )

    @Bean
    fun auditorProvider(): AuditorAware<Jwt> = AuditorAware<Jwt> {
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getPrincipal)
            .filter { it is Jwt }
            .map(Jwt::class::cast)
    }
}
