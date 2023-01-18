enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
        mavenCentral()
    }

    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("deps") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
