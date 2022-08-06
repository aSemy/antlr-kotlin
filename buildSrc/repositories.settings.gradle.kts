@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.jfrog.org/oss-snapshot-local/")
    }

    pluginManagement {
        repositories {
            gradlePluginPortal()
            mavenCentral()
            maven("https://oss.jfrog.org/oss-snapshot-local/")
        }
    }
}
