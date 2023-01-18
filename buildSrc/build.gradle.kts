plugins {
    `kotlin-dsl`
}

dependencies {
    api(deps.gradlePlugins.kotlin)
//    api(deps.gradlePlugins.kotlin.noarg)
    api(deps.gradlePlugins.spring.boot)
//    api(deps.gradlePlugins.kotlin.allopen)
}
