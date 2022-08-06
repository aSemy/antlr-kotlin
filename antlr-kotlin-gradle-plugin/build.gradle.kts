plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

group = "com.github.aSemy.antlr-kotlin-gradle-plugin"

dependencies {
    implementation("org.antlr:antlr4:${Versions.antlr}")
    implementation(projects.antlrKotlinTarget)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}

gradlePlugin {
    plugins {
        create("antlr-kotlin") {
            id = "com.strumenta.antlrkotlin"
            displayName = "Antlr Kotlin"
            description = "support Kotlin as a target for ANTLR"
            implementationClass = "com.strumenta.antlrkotlin.gradleplugin.AntlrKotlinPlugin"
        }
        create("antlr-kotlin-jitpack") {
            id = "com.github.aSemy.antlr-kotlin-gradle-plugin"
            displayName = "Antlr Kotlin"
            description = "support Kotlin as a target for ANTLR"
            implementationClass = "com.strumenta.antlrkotlin.gradleplugin.AntlrKotlinPlugin"
        }
    }
}
