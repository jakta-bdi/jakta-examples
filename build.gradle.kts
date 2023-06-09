plugins {
    kotlin("jvm") version "1.8.20"
    id("org.danilopianini.gradle-kotlin-qa") version "0.38.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("it.unibo.jakta:jakta-dsl:0.6.0")
}

kotlin {
    target {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xcontext-receivers")
            }
        }
    }
}

tasks.detekt {
    onlyIf {
        project.hasProperty("runDetect")
    }
}
tasks.detektMain {
    onlyIf {
        project.hasProperty("runDetect")
    }
}
tasks.cpdKotlinCheck {
    onlyIf {
        project.hasProperty("runCpd")
    }
}
