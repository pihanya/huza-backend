import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(plugin = "kotlin")
apply<KotlinPlatformJvmPlugin>()
apply<JavaBasePlugin>()

/*private*/ val Project.versionCatalog: VersionCatalog
    get() = rootProject.extensions.getByType<VersionCatalogsExtension>().single()

val Project.javaVersion: Provider<JavaVersion>
    get() = provider {
        versionCatalog.findVersion("java").orElseThrow()
            .requiredVersion.let(JavaVersion::toVersion)
    }

val Project.kotlinVersion: Provider<KotlinVersion>
    get() = provider {
        versionCatalog.findVersion("kotlin")
            .orElseThrow().requiredVersion
            .split('.').map(String::toInt)
            .let { (major, minor, patch) -> KotlinVersion(major, minor, patch) }
    }

repositories {
    mavenLocal()
    mavenCentral()
}

extensions.configure<JavaPluginExtension> {
    toolchain {
        vendor.set(JvmVendorSpec.ADOPTIUM)
        languageVersion.set(JavaLanguageVersion.of(javaVersion.get().toString()))
    }
    withJavadocJar()
    withSourcesJar()
}

extensions.configure<KotlinJvmProjectExtension> {
    coreLibrariesVersion = kotlinVersion.get().toString()
    jvmToolchain {
        vendor.set(JvmVendorSpec.ADOPTIUM)
        languageVersion.set(JavaLanguageVersion.of(javaVersion.get().toString()))
    }
}

tasks {
    withType<KotlinCompile>() {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict")
            jvmTarget = javaVersion.get().toString()

            val majorMinor = kotlinVersion.get().run { "$major.$minor" }
            apiVersion = majorMinor
            languageVersion = majorMinor
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    add("implementation", versionCatalog.findBundle("kotlin").get())
}
