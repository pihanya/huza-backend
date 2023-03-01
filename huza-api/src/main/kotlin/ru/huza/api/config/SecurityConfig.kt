package ru.huza.api.config

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
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher
import org.springframework.web.cors.CorsConfiguration
import ru.huza.api.HttpEndpoints
import ru.huza.api.security.JwtAuthenticationEntryPoint
import ru.huza.core.service.UserService


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig {

    @Autowired
    internal lateinit var jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint

    @Autowired
    internal lateinit var userService: UserService

    @Autowired
    internal lateinit var jwtProperties: JwtProperties

    @Bean
    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setPasswordEncoder(passwordEncoder())
            setUserDetailsService(userService)
            setUserDetailsPasswordService(userService)
        }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager =
        http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService<UserDetailsService>(userService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // @formatter:off
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(
                        antMatcher(HttpMethod.GET, "/*"),
                        antMatcher(HttpMethod.GET, "/static/**"),
                        antMatcher(HttpMethod.OPTIONS, "**"),
                        antMatcher("${HttpEndpoints.AUTH}/login"),
                        antMatcher("${HttpEndpoints.AUTH}/info"),
                        antMatcher("${HttpEndpoints.FILES}/download/**"),
                        antMatcher("/auth/login"),
                        antMatcher("/auth/info"),
                    ).permitAll()
                    .requestMatchers(
                        antMatcher(HttpEndpoints.ACTUATOR),
                        antMatcher("${HttpEndpoints.ACTUATOR}/**"),
                    ).hasAuthority("SCOPE_admin")
                    .anyRequest().authenticated()
            }
            .csrf().disable()
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity>::jwt)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .formLogin().disable()
            .cors { cors ->
                cors.configurationSource {
                    CorsConfiguration().applyPermitDefaultValues()
                        .apply { addAllowedMethod(HttpMethod.PATCH) }
                        .apply { addAllowedMethod(HttpMethod.DELETE) }
                }
            }
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAuthenticationEntryPoint)
        // @formatter:on

        return http.build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder =
        BCryptPasswordEncoder()

    @Bean fun jwtDecoder(): JwtDecoder =
        NimbusJwtDecoder.withPublicKey(jwtProperties.publicKey).build()

    @Bean fun jwtEncoder(): JwtEncoder =
        NimbusJwtEncoder(
            ImmutableJWKSet(
                JWKSet(
                    RSAKey.Builder(jwtProperties.publicKey)
                        .privateKey(jwtProperties.privateKey)
                        .build(),
                ),
            ),
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
