@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://github.com/gradle/gradle/issues/22797
plugins {
    id("ru.huza.base")
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    //    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")

//    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation(projects.huzaCore)

    implementation(libs.spring.boot.starter.oauth2ResourceServer)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.boot.starter.security)
}
