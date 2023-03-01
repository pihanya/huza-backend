@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://github.com/gradle/gradle/issues/22797
plugins {
    id("ru.huza.base")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    api(projects.huzaData)
    api(libs.spring.security.core)

    implementation(libs.spring.context)
    implementation(libs.spring.tx)

    implementation(libs.jakarta.annotationApi)

    testRuntimeOnly(libs.jdbcDrivers.postgresql)

    testImplementation(libs.bundles.testing)

    testImplementation(libs.embeddedDatabase.springTest)
    testImplementation(libs.embeddedDatabase.postgres)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.data.jpa)
}
