import java.nio.charset.Charset

group = "it.unibo.jakta"

plugins {
    kotlin("jvm") version "2.0.10"
    id("org.danilopianini.gradle-kotlin-qa") version "0.87.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("it.unibo.jakta:jakta-dsl:0.11.4")
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xcontext-receivers")
    }
}

fun mainFiles() = project
    .projectDir
    .listFiles { f: File -> f.name == "src" }
    ?.firstOrNull()
    ?.walk()
    ?.filter { it.isFile && it.readText(Charset.defaultCharset()).contains("fun main") }
    // ?.map { it.nameWithoutExtension }
    ?.toList()
    ?: emptyList()

fun fromPathToClasspath(file: File) = file
    .path
    .replaceBefore(fromDotToSeparator(project.group.toString()), "")
    .dropLast(3)
    .fromSeparatorToDot()

fun fromDotToSeparator(string: String) = string.replace('.', File.separatorChar)
fun String.fromSeparatorToDot() = this.replace(File.separatorChar, '.')

mainFiles().forEach { mainFile ->
    task<JavaExec>(
        "jakta${
            mainFile.nameWithoutExtension.replaceFirstChar { it.titlecase() }
        }",
    ) {
        group = "JaKtA examples"
        sourceSets.main { classpath = runtimeClasspath }
        mainClass.set(fromPathToClasspath(mainFile))
        standardInput = System.`in`
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
