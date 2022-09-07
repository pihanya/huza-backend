package ru.huza.config

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties {

    lateinit var publicKey: RSAPublicKey

    lateinit var privateKey: RSAPrivateKey

    lateinit var type: String

    lateinit var issuer: String

    lateinit var audience: String
}
