allprojects {
    buildDir = run {
        val globalBuildDir: File = rootProject.projectDir.resolve("build")
        when (project) {
            rootProject -> globalBuildDir.resolve(project.name)
            else -> {
                val relativeProjectPath = projectDir.relativeTo(rootProject.projectDir)
                globalBuildDir.resolve(relativeProjectPath)
            }
        }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
