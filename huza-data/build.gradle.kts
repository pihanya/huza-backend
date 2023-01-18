@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://github.com/gradle/gradle/issues/22797
plugins {
    id("ru.huza.base")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    api(libs.spring.data.commons)

    api(libs.jackson.dataformat.csv)
    api(libs.jackson.datatype.jsr310)
    api(libs.jackson.module.kotlin)

    implementation(libs.spring.boot.starter.data.rest)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.hibernate.envers)

    //implementation(libs.spring.data.envers)
}
