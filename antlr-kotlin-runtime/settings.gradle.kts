rootProject.name = "antlr4-kotlin-runtime"

apply(from = "../buildSrc/repositories.settings.gradle.kts")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}
