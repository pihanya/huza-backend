import org.springframework.boot.gradle.plugin.SpringBootPlugin

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://github.com/gradle/gradle/issues/22797
plugins {
    id("ru.huza.base")
    alias(libs.plugins.kotlin.spring)
}

apply<SpringBootPlugin>()

dependencies {
    runtimeOnly(libs.jdbcDrivers.postgresql)

    implementation(projects.huzaApi)
    implementation(projects.huzaCore)

    implementation(libs.spring.core)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)

    add("developmentOnly", libs.spring.boot.devtools)
    annotationProcessor(libs.spring.boot.configurationProcessor)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
