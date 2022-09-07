package ru.huza.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import org.springframework.security.core.userdetails.UserDetails
import ru.huza.config.JwtProperties

class JwtTokenUtils(
    private val jwtProperties: JwtProperties
) {

    private val jwtParser: JwtParser = Jwts.parserBuilder()
        .setSigningKey(Decoders.BASE64.decode(jwtProperties.secret))
        .requireIssuer(jwtProperties.issuer)
        .requireAudience(jwtProperties.audience)
        .build()

    fun getUsernameFromToken(token: String): String = getClaimFromToken(token) { claims -> claims.subject }

    fun getExpirationDateFromToken(token: String): Date = getClaimFromToken(token) { claims -> claims.expiration }

    fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims = jwtParser.parseClaimsJws(token).body

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate = getExpirationDateFromToken(token)
        return expirationDate.before(Date())
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    /**
     * while creating the token -
     * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to [JWS Compact Serialization](https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     * compaction of the JWT to a URL-safe string
     */
    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuer(jwtProperties.issuer)
            .setAudience(jwtProperties.audience)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY.inWholeMilliseconds))
            .signWith(
                SecretKeySpec(
                    Decoders.BASE64.decode(jwtProperties.secret),
                    SignatureAlgorithm.HS512.jcaName
                )
            )
            .compact()

    companion object {

        private val JWT_TOKEN_VALIDITY: Duration = 5.hours
    }
}
