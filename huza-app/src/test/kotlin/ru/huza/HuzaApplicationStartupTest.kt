package ru.huza

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "spring.datasource.url=jdbc:h2:mem:test",
        "huza.api-url=http://localhost:4242/api/",
        "huza.file-storage-path=.temp/huza-files",
        "jwt.private-key=key.pem",
        "jwt.public-key=cert.pem",
        "jwt.type=JWT",
        "jwt.issuer=ITMO 160RXTX Team",
        "jwt.audience=160RXTX Backend",
    ],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class HuzaApplicationStartupTest : FunSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        test("HuzaApplication startup test") {
            // Nothing to do
        }
    }
}
