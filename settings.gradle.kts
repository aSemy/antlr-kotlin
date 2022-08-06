rootProject.name = "antlr-kotlin"

include(
    ":antlr-kotlin-runtime",
    ":antlr-kotlin-target",
    ":antlr-kotlin-gradle-plugin",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

apply(from = "./buildSrc/repositories.settings.gradle.kts")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}
